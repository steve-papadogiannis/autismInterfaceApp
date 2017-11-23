package com.example.steve.autisminterfaceapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.text.DateFormat;
import java.util.Date;

public class LocationActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener
{
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS=10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS=UPDATE_INTERVAL_IN_MILLISECONDS/2;
    protected final static String REQUESTING_LOCATION_UPDATES_KEY="requesting-location-updates-key";
    protected final static String LOCATION_KEY="location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY="last-updated-time-string-key";
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime,latitude,longitude;
    private Handler handler=new Handler();
    private static ProgressDialog progressBar;
    private static int counter=0;
    private MediaPlayer sound;
    private static int iconId;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_neutral);
        Bundle bundle=getIntent().getExtras();
        iconId=bundle.getInt("iconId");
        mRequestingLocationUpdates=false;
        mLastUpdateTime="";
        updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();
        sound=MediaPlayer.create(this,R.raw.relax);
        AudioManager audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,4,0);
        sound.start();
        createProgressDialog();
    }
    public void createProgressDialog()
    {
        progressBar=new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Παρακαλώ περιμένετε!\nΟι συντεταγμένες στέλνονται στην επαφή που διαλέξατε ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgressNumberFormat(null);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
    }
    public void backButtonClicked(View v)
    {
        zoomAnimation(v);
    }
    private void zoomAnimation(View v)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }
        });
        v.startAnimation(animation);
    }
    public void sendLocation()
    {
        Thread thread=new Thread(new ClientThread(latitude,longitude));
        thread.start();
    }
    private void updateValuesFromBundle(Bundle savedInstanceState)
    {
        if(savedInstanceState!=null)
        {
            // Update the value of mRequestingLocationUpdates from
            // the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly
            // enabled or disabled.
            if(savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY))
            {
                mRequestingLocationUpdates=savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
            }
            // Update the value of mCurrentLocation from
            // the Bundle and update the UI to show the
            // correct latitude and longitude.
            if(savedInstanceState.keySet().contains(LOCATION_KEY))
            {
                // Since LOCATION_KEY was found in the
                // Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation=savedInstanceState.getParcelable(LOCATION_KEY);
            }
                // Update the value of mLastUpdateTime from the Bundle and update the UI.
                if(savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY))
                {
                    mLastUpdateTime=savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
                }
                updateValues();
        }
    }
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                                            .addConnectionCallbacks(this)
                                            .addOnConnectionFailedListener(this)
                                            .addApi(LocationServices.API)
                                            .build();
        createLocationRequest();
    }
    protected void createLocationRequest()
    {
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    public void startUpdates()
    {
        if(!mRequestingLocationUpdates)
        {
            mRequestingLocationUpdates=true;
            Thread thread=new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(50000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    stopUpdates();
                    sendLocation();
                }
            };
            thread.start();
            startLocationUpdates();
        }
    }
    public void stopUpdates()
    {
        if (mRequestingLocationUpdates)
        {
            mRequestingLocationUpdates=false;
            stopLocationUpdates();
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    sound.release();
                    progressBar.dismiss();
                    setContentView(R.layout.location_success);
                    counter=0;
                }
            });
        }
    }
    protected void startLocationUpdates()
    {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    private void updateValues()
    {
        if(mCurrentLocation!=null)
        {
            latitude=String.valueOf(mCurrentLocation.getLatitude());
            longitude=String.valueOf(mCurrentLocation.getLongitude());
        }
    }
    protected void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if(mGoogleApiClient.isConnected()&&mRequestingLocationUpdates)
        {
            startLocationUpdates();
        }
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        if(mGoogleApiClient.isConnected())
        {
            stopLocationUpdates();
        }
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onConnected(Bundle connectionHint)
    {
        if(mCurrentLocation==null)
        {
            mCurrentLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mCurrentLocation.setAccuracy(5.0f);
            mLastUpdateTime=DateFormat.getTimeInstance().format(new Date());
            updateValues();
            startUpdates();
        }
        if(mRequestingLocationUpdates)
        {
            startLocationUpdates();
        }
    }
    @Override
    public void onLocationChanged(Location location)
    {
        mCurrentLocation=location;
        mLastUpdateTime=DateFormat.getTimeInstance().format(new Date());
        updateValues();
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                progressBar.setProgress(counter);
            }
        });
        counter+=20;
    }
    @Override
    public void onConnectionSuspended(int cause)
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                sound.release();
                progressBar.dismiss();
                counter=0;
                setContentView(R.layout.location_failure);
            }
        });
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                sound.release();
                progressBar.dismiss();
                counter=0;
                setContentView(R.layout.location_failure);
            }
        });
    }
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY,mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY,mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }
    public class ClientThread implements Runnable
    {
        private String latitude,longitude;
        private boolean sent=false;
        public ClientThread(String latitude,String longitude)
        {
            this.latitude=latitude;
            this.longitude=longitude;
        }
        public void run()
        {
            try
            {
                if(!sent)
                {
                    GMailSender sender = new GMailSender("",
                            "");
                    switch(iconId)
                    {
                        case R.id.dimitrisIcon:
                            sender.sendMail("Έχω χαθεί!",
                                    "Έχω χαθεί μπορείς να με βοηθήσεις" +
                                            " http://maps.google.com/?q=" + latitude + "," + longitude,
                                    "",
                                    "");
                            sent=true;
                            break;
                        case R.id.eutuxiaIcon:
                            sender.sendMail("Έχω χαθεί!",
                                    "Έχω χαθεί μπορείς να με βοηθήσεις" +
                                            " http://maps.google.com/?q=" + latitude + "," + longitude,
                                    "",
                                    "");
                            sent=true;
                            break;
                        case R.id.eiriniIcon:
                            sender.sendMail("Έχω χαθεί!",
                                    "Έχω χαθεί μπορείς να με βοηθήσεις" +
                                            " http://maps.google.com/?q=" + latitude + "," + longitude,
                                    "",
                                    "");
                            sent=true;
                            break;
                        case R.id.nikiIcon:
                            sender.sendMail("Έχω χαθεί!",
                                    "Έχω χαθεί μπορείς να με βοηθήσεις" +
                                            " http://maps.google.com/?q=" + latitude + "," + longitude,
                                    "",
                                    "");
                            sent=true;
                            break;
                        case -1:
                            sender.sendMail("Έχω χαθεί!",
                                    "Έχω χαθεί μπορείς να με βοηθήσεις" +
                                            " http://maps.google.com/?q=" + latitude + "," + longitude,
                                    "",
                                    "");
                            sent=true;
                            break;
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
