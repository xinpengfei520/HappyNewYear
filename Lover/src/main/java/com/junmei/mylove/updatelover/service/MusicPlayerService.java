package com.junmei.mylove.updatelover.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.Toast;

import com.junmei.mylove.IMusicPlayerService;
import com.junmei.mylove.updatelover.domain.MediaItem;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service {
    private ArrayList<MediaItem> listMediaItems;
    private MediaPlayer mediaPlayer;
    private int position;
    private MediaItem mediaItem;
    public static final String OPENAUDIO = "com.junmei.mobileplayer.OPENAUDIO";


    public MusicPlayerService() {
    }

    private IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {
        MusicPlayerService service = MusicPlayerService.this;

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void setPlaymode(int playmode) throws RemoteException {
            service.setPlaymode(playmode);
        }

        @Override
        public int getPlaymode() throws RemoteException {
            return service.getPlaymode();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return service.getAudioName();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        public boolean isPlaying() throws RemoteException {
            return mediaPlayer.isPlaying();
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        getData();
    }


    private void getData() {
        //因为加载视频是耗时的所以在子线程中进行
        new Thread(){
            @Override
            public void run() {
                super.run();
                listMediaItems=new ArrayList<MediaItem>();
                Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs=new String[]{MediaStore.Video.Media.DISPLAY_NAME, //0
                        MediaStore.Audio.Media.DURATION,  //1
                        MediaStore.Audio.Media.SIZE, //2
                        MediaStore.Audio.Media.ARTIST, //3
                        MediaStore.Audio.Media.DATA //4
                };  //5
                ContentResolver resolver=getContentResolver();
                Cursor cursor=resolver.query(uri, objs, null, null, null);
                if (cursor!=null){
                    while (cursor.moveToNext()){
                        //获取的五个字段都是同一个视频中的，所以要去创建一个MediaItem类
                        String display_name=cursor.getString(0);
                        Long duration=cursor.getLong(1);
                        Long size=cursor.getLong(2);
                        String artist=cursor.getString(3);
                        String data=cursor.getString(4);
                        MediaItem mediaItem=new MediaItem(display_name,duration,size,artist,data);
                        listMediaItems.add(mediaItem);
                    }
                }
                cursor.close();
            }
        }.start();

    }



    //这里千万不能忘记
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }


    private void openAudio(int position) {
        if(listMediaItems!=null&&listMediaItems.size()>0) {
            this.position=position;
            if(position<listMediaItems.size()) {
                mediaItem=listMediaItems.get(position);

                if(mediaPlayer!=null) {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                }

                try {
                mediaPlayer=new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                    mediaPlayer.setOnErrorListener(new MyOnErrorListener());

                    mediaPlayer.setDataSource(mediaItem.getData());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }else{
            Toast.makeText(MusicPlayerService.this, "音频还未加载完成", Toast.LENGTH_SHORT).show();
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            start();
            notifyChange(OPENAUDIO);
        }
    }

    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }

    private void start() {
        mediaPlayer.start();
    }

    private void pause() {
        mediaPlayer.pause();
    }

    private void setPlaymode(int playmode) {

    }

    private int getPlaymode() {

        return 0;
    }

    private String getArtist() {
        if(mediaItem != null){
            return mediaItem.getArtist();
        }
        return "";
    }

    private String getAudioName() {
        if(mediaItem != null){
            return mediaItem.getDisplay_name();
        }
        return "";
    }

    private int getDuration() {
        return 0;
    }

    private int getCurrentPosition() {
        return 0;
    }

    private void seekTo(int position) {

    }

    private void next() {

    }

    private void pre() {

    }

}
