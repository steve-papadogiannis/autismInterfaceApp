package com.example.steve.autisminterfaceapp;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Win2Activity extends Activity
{
    private MediaPlayer applauseSound;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win2);
        applauseSound=MediaPlayer.create(this,R.raw.clapping);
        AudioManager audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,4,0);
        applauseSound.start();
    }
    public void backButtonClicked(View v)
    {
        zoomAnimation(v);
    }
    private void zoomAnimation(View v)
    {
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
        animation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}
            @Override
            public void onAnimationRepeat(Animation animation){}
            @Override
            public void onAnimationEnd(Animation animation)
            {
                applauseSound.release();
                finish();
            }
        });
        v.startAnimation(animation);
    }
}
