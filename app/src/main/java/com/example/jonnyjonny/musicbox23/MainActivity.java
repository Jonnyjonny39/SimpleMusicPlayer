package com.example.jonnyjonny.musicbox23;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer;
    private TextView leftTime;
    private TextView rightTime;
    private Button prevBtn;
    private Button playBtn;
    private Button nextBtn;
    private SeekBar mSeekbar;
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUI();
        mSeekbar.setMax(mediaPlayer.getDuration());
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
                SimpleDateFormat dateFormat=new SimpleDateFormat("mm:ss");
                int currentPos=mediaPlayer.getCurrentPosition();
                int duration=mediaPlayer.getDuration();

                leftTime.setText(dateFormat.format(new Date(currentPos)));
                rightTime.setText(dateFormat.format(new Date(duration-currentPos)));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
    public void setUpUI(){
        mediaPlayer=new MediaPlayer();
        mediaPlayer=MediaPlayer.create(MainActivity.this,R.raw.tpet);

        prevBtn=(Button)findViewById(R.id.prevBtn);
        playBtn=(Button)findViewById(R.id.playBtn);
        nextBtn=(Button)findViewById(R.id.nextBtn);
        mSeekbar=(SeekBar)findViewById(R.id.mSeekbar);

        leftTime=(TextView)findViewById(R.id.leftTime);
        rightTime=(TextView)findViewById(R.id.rightTime);
        prevBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prevBtn:
                backMusic();


                break;
            case R.id.playBtn:
                if(mediaPlayer.isPlaying()){
                    pauseMusic();
                }else {
                    startMusic();
                }
                break;

            case R.id.nextBtn:
                nextMusic();
                break;
        }
    }
    public void pauseMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
            playBtn.setBackgroundResource(android.R.drawable.ic_media_play);
        }


    }
    public void startMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            updateThread();
            playBtn.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }
    public void backMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
        }
    }
    public void nextMusic(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(mediaPlayer.getDuration()-1000);
        }


    }
    public void updateThread(){
        thread=new Thread(){
            @Override
            public void run() {
                try{
                    while (mediaPlayer!=null&&mediaPlayer.isPlaying()){


                    Thread.sleep(50);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int newPosition=mediaPlayer.getCurrentPosition();
                            int newMax=mediaPlayer.getDuration();
                            mSeekbar.setMax(newMax);
                            mSeekbar.setProgress(newPosition);

                            leftTime.setText(String.valueOf(new SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getCurrentPosition()))));
                            rightTime.setText(String.valueOf(new SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getDuration()))));
                        }
                        });


                        }


                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        super.onDestroy();
    }
}
