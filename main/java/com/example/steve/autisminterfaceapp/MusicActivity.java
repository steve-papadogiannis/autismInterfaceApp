package com.example.steve.autisminterfaceapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MusicActivity extends Activity
{
    private ArrayList<Song> songList;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean playbackPaused=false;
    private static int counter;
    private static int songCounter;
    protected Animation animation;
    private ImageView imageView,albumImageView;
    private TextView textView;
    private int coversArray[];
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);
        songList=new ArrayList<>();
        getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        counter=0;
        songCounter=0;
        imageView=(ImageView)findViewById(R.id.imageView5);
        albumImageView=(ImageView)findViewById(R.id.imageView3);
        coversArray=new int[2];
        coversArray[0]=R.mipmap.imhappy;
        coversArray[1]=R.mipmap.letitgo;
        textView=(TextView)findViewById(R.id.textView1);
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
    public void rewindButtonClicked(View v)
    {
        zoomAnimation(v,1);
    }
    public void playButtonClicked(View v)
    {
        zoomAnimation(v,2);
    }
    public void fastForwardButtonClicked(View v)
    {
        zoomAnimation(v,3);
    }
    private void zoomAnimation(final View v,final int sel)
    {
        animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        animation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}
            @Override
            public void onAnimationRepeat(Animation animation){}
            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(sel==1)
                {
                    playPrev();
                }
                else if(sel==2)
                {
                    songPicked(v);
                }
                else
                {
                    playNext();
                }
            }
        });
        v.startAnimation(animation);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(playIntent==null)
        {
            playIntent=new Intent(this,MusicService.class);
            bindService(playIntent,musicConnection,Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }
    private ServiceConnection musicConnection=new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name,IBinder service)
        {
            MusicService.MusicBinder binder=(MusicService.MusicBinder)service;
            musicSrv=binder.getService();
            musicSrv.setList(songList);
            musicSrv.setSong(0);
            albumImageView.setImageDrawable(getResources().getDrawable(coversArray[0]));
        }
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            System.out.println("The service disconnected.");
        }
    };
    public void getSongList()
    {
        ContentResolver musicResolver=getContentResolver();
        Uri musicUri=android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor=musicResolver.query(musicUri,null,null,null,null);
        if(musicCursor!=null&&musicCursor.moveToFirst())
        {
            int titleColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            do
            {
                long thisId=musicCursor.getLong(idColumn);
                String thisTitle=musicCursor.getString(titleColumn);
                String thisArtist=musicCursor.getString(artistColumn);
                if(thisArtist.equals("autism_interface_app"))
                {
                    songList.add(new Song(thisId,thisTitle,thisArtist));
                }
            }
            while(musicCursor.moveToNext());
        }
        if(musicCursor!=null)
        {
            musicCursor.close();
        }
    }
    public void songPicked(View view)
    {
        if(counter==0)
        {
            musicSrv.playSong();
            counter++;
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.pause));
        }
        else
        {
            if(playbackPaused)
            {
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.pause));
                start();
                playbackPaused=false;
            }
            else
            {
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.play));
                pause();
            }
        }
    }
    @Override
    protected void onDestroy()
    {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }
    public void start()
    {
        musicSrv.go();
    }
    public void pause()
    {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }
    private void playNext()
    {
        if(playbackPaused)
        {
            musicSrv.playNext();
            playbackPaused=false;
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.pause));
            songCounter=(++songCounter)%2;
            albumImageView.setImageDrawable(getResources().getDrawable(coversArray[songCounter]));
            textView.setText(String.valueOf(songCounter+1));
        }
        else
        {
            musicSrv.playNext();
            songCounter=(++songCounter)%2;
            albumImageView.setImageDrawable(getResources().getDrawable(coversArray[songCounter]));
            textView.setText(String.valueOf(songCounter+1));
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.pause));
            counter++;
        }
    }
    private void playPrev()
    {
        if(playbackPaused)
        {
            musicSrv.playPrev();
            playbackPaused=false;
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.pause));
            if(songCounter>0)
            {
                songCounter = (--songCounter) % 2;
            }
            else
            {
                songCounter=(++songCounter)%2;
            }
            albumImageView.setImageDrawable(getResources().getDrawable(coversArray[songCounter]));
            textView.setText(String.valueOf(songCounter+1));
        }
        else
        {
            musicSrv.playPrev();
            if(songCounter>0)
            {
                songCounter = (--songCounter) % 2;
            }
            else
            {
                songCounter=(++songCounter)%2;
            }
            albumImageView.setImageDrawable(getResources().getDrawable(coversArray[songCounter]));
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.pause));
            textView.setText(String.valueOf(songCounter+1));
            counter++;
        }
    }
}
