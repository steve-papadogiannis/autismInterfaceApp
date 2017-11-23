package com.example.steve.autisminterfaceapp;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageActivity extends Activity
{
    private MediaPlayer sound;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        Bundle bundle=getIntent().getExtras();
        int id=bundle.getInt("id");
        ImageView imageView=(ImageView)findViewById(R.id.imageView2);
        TextView textView=(TextView)findViewById(R.id.textView);
        switch(id)
        {
            case R.id.eatIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.eat));
                textView.setText(getResources().getString(R.string.eat_message));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.purple));
                sound=MediaPlayer.create(this,R.raw.eat);
                sound.start();
                break;
            case R.id.sleepIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.sleep));
                textView.setText(getResources().getString(R.string.sleep_message));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.purple));
                sound=MediaPlayer.create(this,R.raw.sleep);
                sound.start();
                break;
            case R.id.toiletIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.toilet));
                textView.setText(getResources().getString(R.string.toilet_message));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.purple));
                sound=MediaPlayer.create(this,R.raw.toilet);
                sound.start();
                break;
            case R.id.bathIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.bath));
                textView.setText(getResources().getString(R.string.shower_message));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.purple));
                sound=MediaPlayer.create(this,R.raw.shower);
                sound.start();
                break;
            case R.id.washHandsIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.wash_hands));
                textView.setText(getResources().getString(R.string.washHands));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.purple));
                sound=MediaPlayer.create(this,R.raw.washhands);
                sound.start();
                break;
            case R.id.watchTvIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.watch_tv));
                textView.setText(getResources().getString(R.string.watchTV));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.purple));
                sound=MediaPlayer.create(this,R.raw.watchtv);
                sound.start();
                break;
            case R.id.happyIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.happy));
                textView.setText(getResources().getString(R.string.happy_message));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.turquoise));
                sound=MediaPlayer.create(this,R.raw.happy);
                sound.start();
                break;
            case R.id.painIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.pain));
                textView.setText(getResources().getString(R.string.pain_message));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.turquoise));
                sound=MediaPlayer.create(this,R.raw.pain);
                sound.start();
                break;
            case R.id.sadIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.sad));
                textView.setText(getResources().getString(R.string.sad_message));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.turquoise));
                sound=MediaPlayer.create(this,R.raw.sad);
                sound.start();
                break;
            case R.id.frustratedIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.frustrated));
                textView.setText(getResources().getString(R.string.frustration_message));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.turquoise));
                sound=MediaPlayer.create(this,R.raw.frustrated);
                sound.start();
                break;
            case R.id.boredIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.bored));
                textView.setText(getResources().getString(R.string.bored_message));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.turquoise));
                sound=MediaPlayer.create(this,R.raw.bored);
                sound.start();
                break;
            case R.id.hotIcon:
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.hot));
                textView.setText(getResources().getString(R.string.hot_message));
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.turquoise));
                sound=MediaPlayer.create(this,R.raw.hot);
                sound.start();
                break;
        }
    }
    public void backButtonClicked(View v)
    {
        zoomAnimation(v);
    }
    private void zoomAnimation(View v)
    {
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        animation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}
            @Override
            public void onAnimationRepeat(Animation animation){}
            @Override
            public void onAnimationEnd(Animation animation)
            {
                sound.release();
                finish();
            }
        });
        v.startAnimation(animation);
    }
}
