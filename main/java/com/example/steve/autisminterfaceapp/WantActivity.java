package com.example.steve.autisminterfaceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class WantActivity extends Activity
{
    private Animation animation;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.want);
    }
    public void backButtonClicked(View v)
    {
        zoomAnimation(v);
    }
    private void zoomAnimation(View v)
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
    private void zoomAnimation2(View v,final int id)
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
                intent=new Intent(getApplicationContext(),MessageActivity.class);
                intent.putExtra("id",id);
//                if(id==R.id.imageView3)
//                {
//                    intent.putExtra("selection","eat");
//                }
//                else if(id==R.id.imageView4)
//                {
//                    intent.putExtra("selection","sleep");
//                }
//                else if(id==R.id.imageView5)
//                {
//                    intent.putExtra("selection","toilet");
//                }
//                else if(id==R.id.imageView6)
//                {
//                    intent.putExtra("selection","bath");
//                }
//                else if(id==R.id.imageView7)
//                {
//                    intent.putExtra("selection","wash_hands");
//                }
//                else if(id==R.id.imageView8)
//                {
//                    intent.putExtra("selection","watch_tv");
//                }
                startActivity(intent);
            }
        });
        v.startAnimation(animation);
    }
    public void displayMessage(View v)
    {
        int id=v.getId();
        zoomAnimation2(v,id);
    }
}
