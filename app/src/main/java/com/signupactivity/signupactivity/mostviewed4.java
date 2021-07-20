package com.signupactivity.signupactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Services.OnClearFromRecentService;

public class mostviewed4 extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView mostviewimage4;
    int currentPosition;
    List<Track> tracks;
    NotificationManager notificationManager;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    private BottomSheetBehavior mbottomsheetBehavior;
    ImageView play;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "viewed4";
    ImageView pause;
    SeekBar Scrubber;
    public boolean isplaying=false;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    SeekBar VolumeControl;
    private Handler handler=new Handler();
    int MaxVolume;
    int CurVolume;
    ImageView previous;
    ImageView next;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying())
        {
            prepareAD();
            Toast.makeText(this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            bottomshettplayer.setVisibility(View.INVISIBLE);
            //notificationManager.cancelAll();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        if (null!=info)
        {
            if (info.getType()==ConnectivityManager.TYPE_MOBILE)
            {
                //Toast.makeText(this, "Data Enabled!", Toast.LENGTH_SHORT).show();
            }
            else if (info.getType()==ConnectivityManager.TYPE_WIFI)
            {
                // Toast.makeText(this, "Wifi Enabled!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            startActivity(new Intent(this,nointernetactivity.class));
            finish();
        }
    }

    private ConstraintLayout bottomshettplayer;
    private TextView gaudiotext;
    private TextView gartisttext;
    String audioname;
    String artistid;
    String name;
    Random rand;
    Random rand2;
    int rand_int1;
    int rand_int2;

    Button shuffle;
    private StorageReference storageReferenceMostviewed4;
    TextView songname;
    TextView artistname;
    String[] Viewed_Song4={"https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FSiddhartha%20Corsus%20-%20Beyul.mp3?alt=media&token=892c826f-0efe-4b29-a187-4f19e0c47ca3","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FSiddhartha%20Corsus%20-%20Big%20Blessings.mp3?alt=media&token=6d782186-0cb4-4675-92e0-c6d9d9085fba","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FSiddhartha%20Corsus%20-%20Endless%20Forms%20Most%20Beautiful.mp3?alt=media&token=9726d9df-e445-4a28-809e-1cdadc318355","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FSiddhartha%20Corsus%20-%20Follow%20This%20River%20to%20the%20Sun.mp3?alt=media&token=e42d54c7-565b-4429-821d-840c24cad8a8","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FSiddhartha%20Corsus%20-%20Mountain%20Flowers.mp3?alt=media&token=302801ad-6e6b-422a-8712-a38ac5585537","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FSiddhartha%20Corsus%20-%20Mystic%20Gate.mp3?alt=media&token=57a4727a-a1ea-486d-90dd-2b98386af971","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FSiddhartha%20Corsus%20-%20The%20Diamond%20Way.mp3?alt=media&token=f1e2f75e-8a4f-4693-903c-cc7fcbffba37","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FSiddhartha%20Corsus%20-%20Winds%20of%20Change.mp3?alt=media&token=ab0413a4-3783-4fc7-a309-0e52f3cfbaed","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album4%2Fyt1s.com%20-%20NOCLOUD%20x%20Lil%20Lotus%20%20When%20I%20See%20Your%20Face%20Again.mp3?alt=media&token=257b564b-e6c7-4cb5-b400-d5c3c9444ca2","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album4%2Fyt1s.com%20-%20EDDIE%20FRESCO%20%20FEEL%20THE%20SAME%20Official%20Music%20Video.mp3?alt=media&token=75ee0f1e-5fc4-4835-855c-6124a32b25c4","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album4%2Fyt1s.com%20-%20Shade%20Apollo%20%20Twisted%20Tea%20Smack%20Raps%20prod%20CLPPED.mp3?alt=media&token=3cc2326c-5201-4229-b5f3-d0dd0f45c5de","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album4%2Fyt1s.com%20-%20nev%20ver%20%20Break%20Shit%20Official%20Music%20Video.mp3?alt=media&token=8ae62a88-30f1-45b1-9660-f440b20cbcdf","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album4%2Fyt1s.com%20-%20MAKAVELIGODD%20%20THE%20BULLWORTH%20STALKER%20prod%20HINH.mp3?alt=media&token=61de0829-d315-40d7-b3fd-59175c00747a","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album4%2Fyt1s.com%20-%20XZARKHAN%20%20Facing%20Worlds%20prod%20ninethree.mp3?alt=media&token=9203f7f7-1b46-47cd-8d73-0973b4df6314","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album4%2Fyt1s.com%20-%20Kent%20Osborne%20%20Over%20The%20Limit.mp3?alt=media&token=321e81cb-b1b3-476a-a3a9-9a3cc8cb2a5d","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album4%2Fyt1s.com%20-%20Kylof%20S%C3%B6ze%20%20Suicidewrist.mp3?alt=media&token=66c74a13-f5aa-47d6-80b3-e17d5d301f87"};
    String[] Mostviewed4_Audio = {"Beyul","Big Blessings","Endless Forms Most Beautiful","Follow This River to the Sun"," Mountain Flowers","Mystic Gate","The Diamond Way","Winds of Change","When I See You","FEEL THE SAME","Twisted Tea Smack","Break Shit","WORTH STALKER","Facing Worlds","Over The Limit","Suicidewrist"};
    String[] Mostviewed4_artistname = {"Siddhartha Corsus"};
    int Mostviewed4_images[] = {R.drawable.mc1,R.drawable.mc2,R.drawable.mc3,R.drawable.mc4,R.drawable.mc5,R.drawable.mc6,R.drawable.mc6,R.drawable.mc7,R.drawable.mc8,R.drawable.mc9,R.drawable.mc10,R.drawable.mc11,R.drawable.mc12,R.drawable.mc13,R.drawable.mc14,R.drawable.mc15,R.drawable.mc15};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostviewed4);
        shuffle = (Button) findViewById(R.id.shuffle);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Mostviewed4_Audio.length);
        rand_int2=rand2.nextInt(Mostviewed4_artistname.length)+1;
        shuffle.setOnClickListener(this);
        View  bottomsheet=findViewById(R.id.bottom_sheet_player);
        bottomshettplayer=(ConstraintLayout) findViewById(R.id.bottom_sheet_player);
        mbottomsheetBehavior= BottomSheetBehavior.from(bottomsheet);
        gaudiotext=findViewById(R.id.generalaudioname);
        gartisttext=findViewById(R.id.generalartistname);
        pause=(ImageView)findViewById(R.id.pause);
        play=(ImageView)findViewById(R.id.play);
        previous=(ImageView)findViewById(R.id.previous);
        next=(ImageView)findViewById(R.id.next);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        VolumeControl=(SeekBar)findViewById(R.id.seekBarVolume);
        Scrubber=(SeekBar)findViewById(R.id.seekBarScrubber);
        mediaPlayer=new MediaPlayer();
        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        MaxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        CurVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        intentFilter=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        noisyAudioStreamreceiver=new NoisyAudioStreamreceiver();
        VolumeControl.setMax(MaxVolume);
        VolumeControl.setProgress(CurVolume);
        afChangeListener=new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                    if (mediaPlayer.isPlaying()){
                        playingBeforeInterruption=true;
                    }else
                    {
                        playingBeforeInterruption=false;
                    }
                    mediaPlayer.pause();
                    handler.removeCallbacks(updater);
                    play.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.VISIBLE);
                    //onTrackPause();
                }else if (focusChange==AudioManager.AUDIOFOCUS_GAIN){
                    if (playingBeforeInterruption){
                        mediaPlayer.start();
                        updateSeekBAr();
                        play.setVisibility(View.INVISIBLE);
                        pause.setVisibility(View.VISIBLE);
                      //  onTrackPlay();
                    }
                }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS){
                    mediaPlayer.pause();
                    handler.removeCallbacks(updater);
                    audioManager.abandonAudioFocus(afChangeListener);
                    play.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.VISIBLE);
                   // onTrackPause();
                    audioManager.abandonAudioFocus(afChangeListener);
                }
            }
        };
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(mostviewed4.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }

                });
            }

        });
        if (mediaPlayer.isPlaying())
        {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.INVISIBLE);
            gartisttext.setText(Mostviewed4_artistname[0].toString());
            gaudiotext.setText(Mostviewed4_Audio[currentPosition].toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bottomshettplayer.setVisibility(View.INVISIBLE);
                //notificationManager.cancelAll();
               // prepareAD();

            }
        });
        VolumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        updateSeekBAr();
        populatetrack();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            //createChannel();
            registerReceiver(broadcastReceiver,new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }

        Scrubber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int PlayPosition=(mediaPlayer.getDuration()/100)*Scrubber.getProgress();
                mediaPlayer.seekTo(PlayPosition);
                return false;
            }
        });



        mostviewimage4 = (ImageView) findViewById(R.id.mostviewimage4);
        storageReferenceMostviewed4 = FirebaseStorage.getInstance().getReference().child("Playlist/mostv4.jpg");
      MyAdapter adapter=new MyAdapter(this,Mostviewed4_Audio,Mostviewed4_artistname,Mostviewed4_images);
        ListView mostviewedaudiolist4 = (ListView) findViewById(R.id.mostviewedlistog4);
        mostviewedaudiolist4.setAdapter(adapter);
        mostviewedaudiolist4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Viewed_Song4.length;
                        Uri uri=Uri.parse(Viewed_Song4[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed4_artistname[currentPosition].toString());
                            gaudiotext.setText(Mostviewed4_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Viewed_Song4.length;
                        Uri uri=Uri.parse(Viewed_Song4[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed4_artistname[currentPosition].toString());
                            gaudiotext.setText(Mostviewed4_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=position;
                    audioname=Mostviewed4_Audio[position];
                    name=  Mostviewed4_artistname[0];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Viewed_Song4[position]);
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Viewed_Song4[position]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
/*
                if (isplaying){
                    onTrackPause();
                }
                else{
                    onTrackPlay();
                }*/
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    gartisttext.setText(Mostviewed4_artistname[0]);
                    gaudiotext.setText(Mostviewed4_Audio[position]);

                }else {
                    startActivity(new Intent(mostviewed4.this,nointernetactivity.class));
                }
            }

        });


        final File mostviewedimage1;
        try {
            mostviewedimage1 = File.createTempFile("mostv4", "jpg");
            storageReferenceMostviewed4.getFile(mostviewedimage1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(mostviewedimage1.getAbsolutePath());
                            ((ImageView) findViewById(R.id.mostviewimage4)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mostviewed4.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        bottomshettplayer.setOnClickListener(this);
        mbottomsheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    mbottomsheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }
    private Runnable updater= new Runnable() {
        @Override
        public void run() {
            updateSeekBAr();
            long currentduration=mediaPlayer.getCurrentPosition();
        }
    };
  /*  private void createChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Decibel Inc", NotificationManager.IMPORTANCE_LOW);
            notificationManager=getSystemService(NotificationManager.class);
            if (notificationManager !=null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }*/

    private void populatetrack() {
        tracks=new ArrayList<>();
        tracks.add(new Track("Beyul","Siddhartha Corsus",R.drawable.mc1));
        tracks.add(new Track("Big Blessings","Siddhartha Corsus",R.drawable.mc2));
        tracks.add(new Track("Endless Forms Most Beautiful","Siddhartha Corsus",R.drawable.mc3));
        tracks.add(new Track("Follow This River to the Sun","Siddhartha Corsus",R.drawable.mc4));
        tracks.add(new Track("Mountain Flowers","Siddhartha Corsus",R.drawable.mc5));
        tracks.add(new Track("Mystic Gate","Siddhartha Corsus",R.drawable.mc6));
        tracks.add(new Track("The Diamond Way","Siddhartha Corsus",R.drawable.mc7));
        tracks.add(new Track("Winds of Change","Siddhartha Corsus",R.drawable.mc8));
        tracks.add(new Track("When I See You","Siddhartha Corsus",R.drawable.mc9));
        tracks.add(new Track("FEEL THE SAME","Siddhartha Corsus",R.drawable.mc10));
        tracks.add(new Track("Twisted Tea Smack","Siddhartha Corsus",R.drawable.mc11));
        tracks.add(new Track("Break Shit","Siddhartha Corsus",R.drawable.mc12));
        tracks.add(new Track("WORTH STALKER","Siddhartha Corsus",R.drawable.mc13));
        tracks.add(new Track("Facing Worlds","Siddhartha Corsus",R.drawable.mc14));
        tracks.add(new Track("Over The Limit","Siddhartha Corsus",R.drawable.mc15));
        tracks.add(new Track("Suicidewrist","Siddhartha Corsus",R.drawable.mc16));
    }
    private void prepareAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(mostviewed4.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

    }

    private void updateSeekBAr() {
        if (mediaPlayer.isPlaying())
        {
            Scrubber.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())*100));
            handler.postDelayed(updater,1000);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.play:
                ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    //onTrackPlay();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }else {
                    startActivity(new Intent(mostviewed4.this,nointernetactivity.class));
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying())
                {

                    unregisterReceiver(noisyAudioStreamreceiver);
                    //onTrackPause();
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    pause.setVisibility(View.INVISIBLE);
                    play.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.bottom_sheet_player:
                if (mbottomsheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mbottomsheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mbottomsheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.shuffle:
                if(mediaPlayer.isPlaying())
                {
                    manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                     info=manager.getActiveNetworkInfo();
                    if (null!=info)
                    {
                        mediaPlayer.reset();
                        try {

                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            mediaPlayer.setDataSource(Viewed_Song4[rand_int2]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            shuffle.setVisibility(View.INVISIBLE);
                            CreateNotification.createNotification(mostviewed4.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed4_artistname[0]);
                            gaudiotext.setText(Mostviewed4_Audio[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(mostviewed4.this,nointernetactivity.class));
                    }
                    /*if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }*/
                }
                else
                {
                    manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    info=manager.getActiveNetworkInfo();
                    if (null!=info)
                    {
                        mediaPlayer.reset();
                        try {

                            unregisterReceiver(noisyAudioStreamreceiver);
                            shuffle.setVisibility(View.INVISIBLE);
                            mediaPlayer.setDataSource(Viewed_Song4[rand_int1]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            CreateNotification.createNotification(mostviewed4.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed4_artistname[0]);
                            gaudiotext.setText(Mostviewed4_Audio[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(mostviewed4.this,nointernetactivity.class));
                    }
                   /* if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }*/
                }
        }
    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");
            switch (action) {
                case CreateNotification.ACTION_PLAY:
                    if (isplaying) {
                        onTrackPause();
                    } else {
                        onTrackPlay();
                    }
                    break;

            }
        }
    };
    private class NoisyAudioStreamreceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())){
                mediaPlayer.pause();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
               notificationManager.cancelAll();
                handler.removeCallbacks(updater);

            }
        }
    }

    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(mostviewed4.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
        mediaPlayer.start();
        updateSeekBAr();
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.VISIBLE);
        isplaying=true;

    }

    @Override
    public void onTrackPause() {
        if(mediaPlayer.isPlaying())
        {
            CreateNotification.createNotification(mostviewed4.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
            isplaying=false;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
            //notificationManager.cancelAll();
        unregisterReceiver(broadcastReceiver);
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String Mostviewed4_Audio[];
        String Mostviewed4_Artistname[];
        int Mostviewed4_images[];


        public MyAdapter(@NonNull Context c, String songname[], String artistname[], int images[]) {
            super(c, R.layout.tuckerlistitem, R.id.tuckeraudioname, songname);
            this.context = c;
            this.Mostviewed4_Audio = songname;
            this.Mostviewed4_Artistname = artistname;
            this.Mostviewed4_images = images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listitem = layoutInflater.inflate(R.layout.tuckerlistitem, parent, false);
            ImageView imageView = listitem.findViewById(R.id.audioimage);
            songname = listitem.findViewById(R.id.tuckeraudioname);
            artistname = listitem.findViewById(R.id.tuckeraudioartistname);


            //imageView.setImageResource(Mostviewed1_images[position]);
            songname.setText(Mostviewed4_Audio[position].toString());
            imageView.setImageResource(Mostviewed4_images[position]);
            artistname.setText(Mostviewed4_Artistname[0].toString());
            return listitem;
        }
    }
    }
