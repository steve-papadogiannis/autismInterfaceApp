package com.example.steve.autisminterfaceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SelectionActivity extends Activity
{
    private Animation animation;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection);
    }
    public void backButtonClicked(View v)
    {
        zoom2Animation(v);
    }
    private void zoom2Animation(View v)
    {
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        animation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}
            @Override
            public void onAnimationRepeat(Animation animation){}
            @Override
            public void onAnimationEnd(Animation animation)
            {
                finish();
            }
        });
        v.startAnimation(animation);
    }
    public void oneClicked(View v)
    {
        zoomAnimation(v,DrawingFun.class);
    }
    private void zoomAnimation(View v,final Class<?> c)
    {
        animation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
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
    public void twoClicked(View v)
    {
        zoomAnimation(v,GameActivity.class);
    }
}
