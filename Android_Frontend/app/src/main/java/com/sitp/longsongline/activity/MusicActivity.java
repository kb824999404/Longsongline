package com.sitp.longsongline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sitp.longsongline.R;
import com.sitp.longsongline.Service.MusicController;
import com.sitp.longsongline.api.ApiConfig;
import com.sitp.longsongline.entity.Music;

import java.io.IOException;
import java.util.Date;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,Runnable {
    private Music music;

    private SeekBar mSeekBar;
    private TextSwitcher mSwitcher;

    private MusicController mMusicController;

    private QMUIRoundButton playingButton;
    private boolean isPlaying = false;
    private boolean running = false;

    private static final String TAG="MusicActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        //隐藏默认标题栏
        getSupportActionBar().hide();
        Init();
    }

    private void Init() {
        //设置标题栏标题
        QMUITopBar topbar = (QMUITopBar) findViewById(R.id.topbar);
        topbar.setTitle("我的音乐");

        Intent intent = getIntent();
        music=(Music)intent.getSerializableExtra("music");

        TextView title = (TextView)findViewById(R.id.music_title);
        title.setText(music.getTitle());

        String content = music.getContent();
        if(content.length()<20){
            int n_pad = 20-content.length();
            for(int i=0;i<n_pad;i++){
                content+=" ";
            }
        }
        System.out.println(content.length());
        String line1 = content.substring(0,5) + "，"+content.substring(5,10) + "。";
        String line2 = content.substring(10,15) + "，"+content.substring(15,20) + "。";
        TextView line1_tv = (TextView)findViewById(R.id.music_line1);
        TextView line2_tv = (TextView)findViewById(R.id.music_line2);
        line1_tv.setText(line1);
        line2_tv.setText(line2);

        InitMusic();
    }

    private void InitMusic(){
        //滑动条部分
        mSeekBar = (SeekBar) findViewById(R.id.music_seek_bar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSwitcher = (TextSwitcher) findViewById(R.id.text_switcher);
        mSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mSwitcher.setOutAnimation(this, android.R.anim.fade_out);

        playingButton=(QMUIRoundButton)findViewById(R.id.playing_play);
        playingButton.setOnClickListener(this);
        playingButton.setText("加载中");
        playingButton.setEnabled(false);

        MediaPlayer mediaPlayer=new MediaPlayer();
        String url=ApiConfig.BASE_URl+ApiConfig.STATIC_SONGS+"/"+music.getPath()+".wav";
        Log.d(TAG,url);
        mMusicController = new MusicController(url);
        mMusicController.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playingButton.setEnabled(true);
                playingButton.setText("播放");
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.playing_play:
                if (isPlaying){
                    pause();
                }else {
                    play();
                }
                break;
        }
    }
    private void play(){
        Log.d(TAG,isPlaying+"");
        playingButton.setText("暂停");
        mMusicController.play();//播放
        isPlaying = true;
    }
    private void pause(){
        playingButton.setText("播放");
        mMusicController.pause();
        isPlaying = false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onStop() {
        running = false;
        super.onStop();
    }

    @Override
    public void run() {
        running = true;
        try {
            while (running) {
                if (mMusicController != null) {
                    long musicDuration = mMusicController.getMusicDuration();
                    final long position = mMusicController.getPosition();
                    final Date dateTotal = new Date(musicDuration);
                    final SimpleDateFormat sb = new SimpleDateFormat("mm:ss");
                    mSeekBar.setMax((int) musicDuration);
                    mSeekBar.setProgress((int) position);
                    mSwitcher.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Date date = new Date(position);
                                    String time = sb.format(date) + "/" + sb.format(dateTotal);
                                    mSwitcher.setCurrentText(time);
                                }
                            }
                    );
                }

                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMusicController.setPosition(seekBar.getProgress());
    }
}