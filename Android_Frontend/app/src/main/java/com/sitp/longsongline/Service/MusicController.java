package com.sitp.longsongline.Service;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;

import java.io.IOException;

public class MusicController  extends Binder {
    private MediaPlayer mPlayer;
    public MusicController(String Url){
        mPlayer = new MediaPlayer();
        try{
            mPlayer.setDataSource(Url);
            mPlayer.setAudioAttributes(
                    new AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());
            mPlayer.prepareAsync();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void play() {
        mPlayer.start();//开启音乐
    }
    public void pause() {
        mPlayer.pause();//暂停音乐
    }
    public long getMusicDuration() {
        return mPlayer.getDuration();//获取文件的总长度
    }
    public long getPosition() {
        return mPlayer.getCurrentPosition();//获取当前播放进度
    }
    public void setPosition (int position) {
        mPlayer.seekTo(position);//重新设定播放进度
    }
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener){
        mPlayer.setOnPreparedListener(listener);
    }
}
