package com.example.steve.autisminterfaceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends Activity
{
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startEgoActivity(View v)
    {
        zoomAnimation(v,EgoActivity.class);
    }
    public void startSosActivity(View v)
    {
        zoomAnimation(v,SosActivity.class);
    }
    public void startWantActivity(View v)
    {
        zoomAnimation(v,WantActivity.class);
    }
    public void startFeelingsActivity(View v)
    {
        zoomAnimation(v,FeelingsActivity.class);
    }
    public void startMusicActivity(View v)
    {
        zoomAnimation(v,MusicActivity.class);
    }
    public void startGameActivity(View v)
    {
        zoomAnimation(v,SelectionActivity.class);
    }
    private void zoomAnimation(View v,final Class<?> c)
    {
        Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        animation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}
            @Override
            public void onAnimationRepeat(Animation animation){}
            @Override
            public void onAnimationEnd(Animation animation)
            {
                intent=new Intent(getApplicationContext(),c);
                startActivity(intent);
            }
        });
        v.startAnimation(animation);
    }
}
