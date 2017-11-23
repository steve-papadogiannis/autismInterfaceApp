package com.example.steve.autisminterfaceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SosActivity extends Activity
{
    private Animation animation;
    private static int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos);
    }
    public void backButtonClicked(View v)
    {
        zoomAnimation(v);
    }
    private void zoomAnimation(View v)
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
                finish();
            }
        });
        v.startAnimation(animation);
    }
    private void zoom2Animation(final View v)
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
                Intent intent=new Intent(getApplicationContext(),LocationActivity.class);
                intent.putExtra("iconId",v.getId());
                startActivity(intent);
            }
        });
        v.startAnimation(animation);
    }
    public void contactClicked(View v)
    {
        zoom2Animation(v);
    }
    public void sosClicked(View v)
    {
        if(counter<5)
        {
            ++counter;
        }
        else
        {
            Intent intent=new Intent(getApplicationContext(),LocationActivity.class);
            intent.putExtra("iconId",-1);
            startActivity(intent);
        }
    }
}
