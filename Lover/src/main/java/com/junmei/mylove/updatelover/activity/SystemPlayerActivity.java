package com.junmei.mylove.updatelover.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.junmei.mylove.R;
import com.junmei.mylove.updatelover.domain.MediaItem;
import com.junmei.mylove.updatelover.utils.TimeUtils;
import com.junmei.mylove.updatelover.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SystemPlayerActivity extends Activity implements View.OnClickListener {

    private VideoView videoview;
    private Uri uri;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVideoVoice;
    private SeekBar seekbarVoice;
    private Button btnVideoSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrent;
    private SeekBar seekbarVedio;
    private TextView tvDuration;
    private Button btnVideoExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSwichScreen;
    private MyBroadcastReceiver receiver;
    private ArrayList<MediaItem> mediaItems;
    private int position;
    private GestureDetector detector;
    private boolean isShowMediaController = false;

    private static final int HIDE_MEDIACONTROLLER = 2;
    private static final int SCREEN_DEFULT = 22;
    private static final int SCREEN_FULL = 11;

    private boolean isFullScreen = false;
    //屏幕的宽和高
    private int screenWidth;
    private int screenHeight;

    //视频的真实宽高
    private int videoWidth;
    private int videoHeight;

    private AudioManager am; //用来调节音量的
    private int currentVolume;  //当前音量
    private int maxVolume;  //最大音量

    private boolean isMute = false;  //默认不是静音

    /**
     * 记录按下这个时刻的当前音量
     */
    private int mVol;
    /**
     * 总距离
     */
    private int rangTouch;
    private float startY;


    private void findViews() {
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        btnVideoVoice = (Button) findViewById(R.id.btn_video_voice);
        seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        btnVideoSwitchPlayer = (Button) findViewById(R.id.btn_video_switch_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrent = (TextView) findViewById(R.id.tv_current);
        seekbarVedio = (SeekBar) findViewById(R.id.seekbar_vedio);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnVideoExit = (Button) findViewById(R.id.btn_video_exit);
        btnVideoPre = (Button) findViewById(R.id.btn_video_pre);
        btnVideoStartPause = (Button) findViewById(R.id.btn_video_start_pause);
        btnVideoNext = (Button) findViewById(R.id.btn_video_next);
        btnVideoSwichScreen = (Button) findViewById(R.id.btn_video_swich_screen);

        btnVideoVoice.setOnClickListener(this);
        btnVideoSwitchPlayer.setOnClickListener(this);
        btnVideoExit.setOnClickListener(this);
        btnVideoPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);
        btnVideoNext.setOnClickListener(this);
        btnVideoSwichScreen.setOnClickListener(this);

        seekbarVoice.setMax(maxVolume);
        seekbarVoice.setProgress(currentVolume);
    }

    //点击事件的监听
    @Override
    public void onClick(View v) {
        if (v == btnVideoVoice) {
            isMute = !isMute;
            updateVolume(currentVolume);

        } else if (v == btnVideoSwitchPlayer) {
            // Handle clicks for btnVideoSwitchPlayer
        } else if (v == btnVideoExit) {
            finish();
        } else if (v == btnVideoPre) {
            setPlayPreVideo();
        } else if (v == btnVideoStartPause) {
            setStartAndPanse();
        } else if (v == btnVideoNext) {
            setPlayNextVideo();
        } else if (v == btnVideoSwichScreen) {
            setVideoMode();
        }

        //在所有点击事件中也要设置移除消息，？？？重发又是为什么
        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 2000);
    }

    private void setStartAndPanse() {
        if (videoview.isPlaying()) {
            videoview.pause();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        } else {
            videoview.start();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }


    ////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemplayer);
        videoview = (VideoView) findViewById(R.id.videoview);

        initData();

        findViews();

        //////////////
        getData();
        setListener();
        setData();
        //设置控制面板,注释掉，则系统的控制面板就不会有了，就会只显示自己的了
        //videoview.setMediaController(new MediaController(this));


    }

    private void setData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //有数据则从列表中get
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getDisplay_name());///
            videoview.setVideoPath(mediaItem.getData());
        } else if (uri != null) {
            //设置播放地址, 文件夹或者第三方应用的请求
            videoview.setVideoURI(uri);
            tvName.setText(uri.toString());
        } else {
            Toast.makeText(SystemPlayerActivity.this, "没有传递数据过来", Toast.LENGTH_SHORT).show();
        }

        //设置按钮状态
        setButtonState();
    }

    private void getData() {
        uri = getIntent().getData();  //这个其实就是视频播放地址,这行代码保留，为了文件等来播放视频
        //得到视频列表数据
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("medialist");
        //列表传过来后，还要知道是哪一个
        position = getIntent().getIntExtra("position", 0);
    }

    private void setListener() {
        //接着要做一些视频播放的监听：播放准备好、播放出错、播放完成
        videoview.setOnPreparedListener(new MyOnPreparedListener());
        videoview.setOnErrorListener(new MyOnErrorListener());
        videoview.setOnCompletionListener(new MyOnCompletionListener());
        //设置视频进度seekbar拖动的监听
        seekbarVedio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

        //设置音量拖动的监听
        seekbarVoice.setOnSeekBarChangeListener(new VolumeOnSeekBarChangeListener());

    }

    private void initData() {
        timeUtils = new TimeUtils();
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

        //创建手势识别器
        detector = new GestureDetector(this, new MySimpleOnGestureListener());

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;

        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


    }

    class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            setStartAndPanse();
            super.onLongPress(e);
        }


        @Override
        public boolean onDoubleTap(MotionEvent e) {
            setVideoMode();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isShowMediaController) {
                hideMediaController();
                handler.removeMessages(HIDE_MEDIACONTROLLER);
            } else {
                showMediaController();
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4200);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    private void setVideoMode() {
        if (isFullScreen) {
            //若是全屏，则设置默认
            setVideoType(SCREEN_DEFULT);
        } else {
            //若是默认，则设置全屏
            setVideoType(SCREEN_FULL);
        }
    }

    private void setVideoType(int videoType) {
        switch (videoType) {
            case SCREEN_FULL:
                isFullScreen = true;
                videoview.setVideoSize(screenWidth, screenHeight);
                btnVideoSwichScreen.setBackgroundResource(R.drawable.btn_video_swich_screen_defualt_selector);
                break;
            case SCREEN_DEFULT:
                isFullScreen = false;

                //真实视频的宽和高
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;

                //屏幕的真实宽和高
                int width = screenWidth;
                int height = screenHeight;

                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }


                videoview.setVideoSize(width, height);
                btnVideoSwichScreen.setBackgroundResource(R.drawable.btn_video_swich_screen_full_selector);
                break;
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            setBattery(level);
        }
    }

    private void setBattery(int level) {
        if (level <= 0) {
            ivBattery.setBackgroundResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setBackgroundResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    class VolumeOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                updateProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
        }
    }

    private void updateProgress(int progress) {

        am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        currentVolume = progress;
        seekbarVoice.setProgress(progress);

        if (progress > 0) {
            isMute = false;
        } else {
            isMute = true;
        }

    }

    private void updateVolume(int progress) {
        if (isMute) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarVoice.setProgress(0);
        } else {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            currentVolume = progress;
            seekbarVoice.setProgress(progress);
        }

    }


    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {  //保证只有是用户操作时才响应一次
                videoview.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //在拖拽的过程中不允许隐藏，所以一触摸时就移除消息
            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //一up离开时就重新发消息
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
        }
    }

    //控制面板隐藏显示的函数
    private void hideMediaController() {
        isShowMediaController = false;
        llTop.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
    }

    private void showMediaController() {
        isShowMediaController = true;
        llTop.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
    }


    private static final int PROGRESS = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    int currentPosition = videoview.getCurrentPosition();
                    seekbarVedio.setProgress(currentPosition);
                    tvCurrent.setText(timeUtils.stringForTime(currentPosition));
                    //得到系统时间
                    tvSystemTime.setText(getSystemTime());
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
                case HIDE_MEDIACONTROLLER:  //自动隐藏控制面板
                    hideMediaController();
                    break;
            }
        }
    };

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date());
    }

    private TimeUtils timeUtils;

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();


            videoview.start();

            //得到视频总时长
            int duration = videoview.getDuration();
            seekbarVedio.setMax(duration);

            tvDuration.setText(timeUtils.stringForTime(duration));

            //默认准备好进来时，控制面板是隐藏的
            hideMediaController();

            handler.sendEmptyMessage(PROGRESS);

            //测试自定义控制面板
            // videoview.setVideoSize(700,700);
            setVideoType(SCREEN_DEFULT);
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(SystemPlayerActivity.this, "播放出错了", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // finish();
            setPlayNextVideo();
        }
    }

    private void setPlayPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position >= 0) {
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getDisplay_name());
                videoview.setVideoPath(mediaItem.getData());
                setButtonState();
            } else {
                position = 0;
            }
        }
    }


    private void setPlayNextVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position < mediaItems.size()) {
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getDisplay_name());
                videoview.setVideoPath(mediaItem.getData());

                setButtonState();

                if (position == mediaItems.size() - 1) {
                    Toast.makeText(SystemPlayerActivity.this, "播放最后一个", Toast.LENGTH_SHORT).show();
                }
            } else {
                position = mediaItems.size() - 1;
                finish();
            }
        } else if (uri != null) {   //传的只有一个播放地址
            finish();
        }
    }

    private void setButtonState() {
        if (mediaItems != null && mediaItems.size() > 0) {
            setIsEnableButton(true);
            if (position == 0) {
                btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnVideoPre.setEnabled(false);
            }

            if (position == mediaItems.size() - 1) {
                btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnVideoNext.setEnabled(false);
            }
        } else if (uri != null) {
            setIsEnableButton(false);
        }
    }

    private void setIsEnableButton(boolean enable) {
        if (enable) {
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
        } else {
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
        }
        btnVideoPre.setEnabled(enable);
        btnVideoNext.setEnabled(enable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //把事件传入手势识别器
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录当前按下时的音量，当前按下时的位置,总距离
                mVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                startY = event.getY();
                rangTouch = Math.min(screenWidth, screenHeight);
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE:
                //记录此时的位置，求得差值位移，求得改变音量，设置新的音量
                float endY = event.getY();
                float distanceY = startY - endY;
                float delat = (int) ((distanceY / rangTouch) * maxVolume);
                int volume = (int) Math.min(Math.max(mVol + delat, 0), maxVolume);
                if (delat != 0) {
                    updateProgress(volume);   //???为什么不是updateVolume();
                }
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            currentVolume++;
            updateProgress(currentVolume);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            currentVolume--;
            updateProgress(currentVolume);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            return true;
        }


        return true;
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }

        super.onDestroy();
    }
}
