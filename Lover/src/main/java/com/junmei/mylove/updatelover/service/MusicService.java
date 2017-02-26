package com.junmei.mylove.updatelover.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.junmei.mylove.R;

public class MusicService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    MediaPlayer player;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getStringExtra("action");
        if("play".equals(action)) {
            play();
        }else if("play1".equals(action)){
            play1();
        }else if("pause".equals(action)){
            pause();
        }else if("stop".equals(action)){
            stop();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void pause() {
        if (player != null) {
            player.pause();

        }
    }

    private void stop() {
        if (player != null) {
            player.stop();
            player.reset();
            player.release();//释放加载的文件
            player = null;//不要忘了！
        }
    }

    private void play() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.loveyourself);
            player.setLooping(true);

        }
        if (player != null && !player.isPlaying()) {
            player.start();

        }
    }

    private void play1() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.xiaoxingyun);
            player.setLooping(true);

        }
        if (player != null && !player.isPlaying()) {
            player.start();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();//停止音乐
    }
}
