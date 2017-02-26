package com.junmei.mylove.updatelover.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.junmei.mylove.IMusicPlayerService;
import com.junmei.mylove.R;
import com.junmei.mylove.updatelover.service.MusicPlayerService;
import com.junmei.mylove.updatelover.utils.TimeUtils;

public class AudioPlayerActivity extends Activity implements View.OnClickListener {
    //    private ImageView iv_icon;
//    private int position;
    private IMusicPlayerService service;

    ///////////////
    /**
     * 进度更新
     */
    private static final int PROGERSS = 1;
    private ImageView iv_icon;
    private int position;
    private TextView tvArtist;
    private TextView tvName;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnAudioSwichLyricCover;
    private MyBroadcastReceiver receiver;

    private RelativeLayout ig_story;
    private ImageView ig_item;
    private ImageView ig_dis;
    // 渐变的图片
    private Drawable[] drawables;
    // 动画
    private Animation[] animations;
    // 记录当前图片的index
    private int currentItem;

    private TimeUtils timeUtils;

    private void findViews() {
        setContentView(R.layout.activity_audio_player);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        seekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        btnAudioPlaymode = (Button) findViewById(R.id.btn_audio_playmode);
        btnAudioPre = (Button) findViewById(R.id.btn_audio_pre);
        btnAudioStartPause = (Button) findViewById(R.id.btn_audio_start_pause);
        btnAudioNext = (Button) findViewById(R.id.btn_audio_next);
        btnAudioSwichLyricCover = (Button) findViewById(R.id.btn_audio_swich_lyric_cover);

        ig_story = (RelativeLayout) findViewById(R.id.ig_story);
        ig_item = (ImageView) findViewById(R.id.ig_item);
        ig_dis = (ImageView) findViewById(R.id.ig_dis);

        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_icon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_icon.getBackground();
        animationDrawable.start();

        btnAudioPlaymode.setOnClickListener(this);
        btnAudioPre.setOnClickListener(this);
        btnAudioStartPause.setOnClickListener(this);
        btnAudioNext.setOnClickListener(this);
        btnAudioSwichLyricCover.setOnClickListener(this);


        ig_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ig_dis.setVisibility(View.GONE);
                startIgAnimation(ig_item);
            }
        });

        // 设置音频进度的拖拽
        // seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }


    private void startIgAnimation(ImageView imageView) {
        drawables = new Drawable[]{
                getResources().getDrawable(R.drawable.pic1),
                getResources().getDrawable(R.drawable.pic2),
                getResources().getDrawable(R.drawable.pic3),
                getResources().getDrawable(R.drawable.pic4),
                getResources().getDrawable(R.drawable.pic5),
                getResources().getDrawable(R.drawable.pic6),
                getResources().getDrawable(R.drawable.pic7)};

        animations = new Animation[]{
                AnimationUtils.loadAnimation(this, R.anim.guide_start),
                AnimationUtils.loadAnimation(this, R.anim.guide_ing),
                AnimationUtils.loadAnimation(this, R.anim.guide_end)};

        // 设置动画持续时间和监听
        for (int i = 0; i < animations.length; i++) {
            animations[i].setDuration(1500);
            animations[i].setAnimationListener(new MyAnimationListener(i));
        }

        ig_item.setImageDrawable(drawables[0]);
        ig_item.setAnimation(animations[0]);
    }


    private class MyAnimationListener implements Animation.AnimationListener {

        private int index;

        public MyAnimationListener(int index) {
            this.index = index;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        // 动画结束调用
        @Override
        public void onAnimationEnd(Animation animation) {
            if (index < (animations.length - 1)) {
                ig_item.startAnimation(animations[index + 1]);
            } else {
                currentItem++;
                if (currentItem > (drawables.length - 1)) {
                    currentItem = 0;//会循环
                    //      ig_story.setVisibility(View.GONE);//

                    //不会循环，播完直接进入
                    // Intent intent=new Intent(MainActivity.this, FindActivity.class);
                    //  startActivity(intent);
                    //
                }
                ig_item.setImageDrawable(drawables[currentItem]);
                ig_item.startAnimation(animations[0]);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


//////////////

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGERSS:
                    int currentPosition = 0;
                    try {
                        currentPosition = service.getCurrentPosition();
                        //更新时间进度
                        tvTime.setText(timeUtils.stringForTime(currentPosition) + "/" + timeUtils.stringForTime(service.getDuration()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    seekbarAudio.setProgress(currentPosition);

                    removeMessages(PROGERSS);
                    sendEmptyMessageDelayed(PROGERSS, 1000);
                    break;
            }
        }
    };


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = IMusicPlayerService.Stub.asInterface(binder);//得到服务代理类

            //开始播放音乐
            try {
                service.openAudio(position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            //得到歌曲名称和演唱者名称并且显示
            showData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void showData() {
        try {
            tvArtist.setText(service.getArtist());
            tvName.setText(service.getAudioName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        initData();
        findViews();
        getData();
        startAndBindService();
    }

    private void initData() {
        //注册广播
        receiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.OPENAUDIO);
        registerReceiver(receiver, intentFilter);
        timeUtils = new TimeUtils();
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //歌曲开始更新
            showProgress();
        }
    }

    private void showProgress() {
        try {
            seekbarAudio.setMax(service.getDuration());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(PROGERSS);
    }


    private void startAndBindService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.junmei.mobileplayer.OPENAUDIO");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);//防止服务多次实例化
    }

    private void getData() {
        position = getIntent().getIntExtra("position", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
            conn = null;
        }

        //取消注册广播
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnAudioPlaymode) {
            // Handle clicks for btnAudioPlaymode
        } else if (v == btnAudioPre) {
            // Handle clicks for btnAudioPre
        } else if (v == btnAudioStartPause) {
            try {
                if (service.isPlaying()) {
                    service.pause(); //暂停
                    // 按钮设置-播放状态
                    btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                } else {
                    service.start(); // 播放
                    // 按钮设置-暂停
                    btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (v == btnAudioNext) {

        } else if (v == btnAudioSwichLyricCover) {

        }
    }

}
