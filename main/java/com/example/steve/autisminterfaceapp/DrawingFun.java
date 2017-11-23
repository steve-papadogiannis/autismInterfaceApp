package com.example.steve.autisminterfaceapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.UUID;

public class DrawingFun extends Activity implements View.OnClickListener
{
    private DrawingView drawView;
    private float smallBrush,mediumBrush,largeBrush;
    private ImageButton currPaint;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing_fun);
        drawView=(DrawingView)findViewById(R.id.drawing);
        LinearLayout paintLayout=(LinearLayout)findViewById(R.id.paint_colors);
        currPaint=(ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        smallBrush=getResources().getInteger(R.integer.small_size);
        mediumBrush=getResources().getInteger(R.integer.medium_size);
        largeBrush=getResources().getInteger(R.integer.large_size);
        ImageButton drawBtn=(ImageButton)findViewById(R.id.brush_btn);
        drawBtn.setOnClickListener(this);
        drawView.setBrushSize(mediumBrush);
        ImageButton eraseBtn=(ImageButton)findViewById(R.id.eraser_btn);
        eraseBtn.setOnClickListener(this);
        ImageButton newBtn=(ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        ImageButton saveBtn=(ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
        mediaPlayer=MediaPlayer.create(this,R.raw.relax3);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        AudioManager audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,5,0);
    }
    public void paintClicked(View view)
    {
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        if(view!=currPaint)
        {
            ImageButton imgView=(ImageButton)view;
            String color=view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.brush_btn)
        {
            final Dialog brushDialog=new Dialog(this);
            brushDialog.setTitle("Μέγεθος Πινέλου:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn=(ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn=(ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn=(ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(v.getId()==R.id.eraser_btn)
        {
            final Dialog brushDialog=new Dialog(this);
            brushDialog.setTitle("Μέγεθος Γόμας:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn=(ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn=(ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn=(ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(v.getId()==R.id.new_btn)
        {
            drawView.startNew();
        }
        else if(v.getId()==R.id.save_btn)
        {
            drawView.setDrawingCacheEnabled(true);
            String imgSaved= MediaStore.Images.Media.insertImage(getContentResolver(),drawView.getDrawingCache(),UUID.randomUUID().toString()+".png","drawing");
            if(imgSaved!=null)
            {
                Toast savedToast= Toast.makeText(getApplicationContext(), "Το σχέδιο αποθηκεύτηκε στην Συλλογή!", Toast.LENGTH_SHORT);
                savedToast.show();
            }
            else
            {
                Toast unsavedToast=Toast.makeText(getApplicationContext(),"Το σχέδιο δεν μπορεί να αποθηκευτεί!",Toast.LENGTH_SHORT);
                unsavedToast.show();
            }
            drawView.destroyDrawingCache();
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
                mediaPlayer.release();
                finish();
            }
        });
        v.startAnimation(animation);
    }
}
