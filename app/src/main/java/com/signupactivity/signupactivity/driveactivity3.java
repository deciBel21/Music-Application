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

public class driveactivity3 extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView driveimage3;
    private BottomSheetBehavior mbottomsheetBehavior;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    ImageView play;
    List<Track> tracks;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "drive3";
    NotificationManager notificationManager;
    int currentPosition;
    ImageView pause;
    Random rand2;
    ImageView previous;
    ImageView next;
    SeekBar Scrubber;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    SeekBar VolumeControl;
    private Handler handler=new Handler();
    private ConstraintLayout bottomshettplayer;
    private TextView gaudiotext;
    private TextView gartisttext;
    String audioname;
    String artistid;
    String name;
    Random rand;
    int rand_int1;
    int rand_int2;
    int MaxVolume;
    int CurVolume;
    public boolean isplaying=false;
    ConnectivityManager manager;

    Button shuffle;
    private StorageReference storageReferenceDrive3;
    TextView songname;
    TextView artistname;
    String[] Drive_Song3={"https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2FGlimlip%20%26%20TwoFiveOne%20-%20Five%20Friends%20%5BFull%20EP%5D.mp3?alt=media&token=0b705ac1-af37-43e3-ad2f-4d8d22fc6d30","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2FMaoen%20-%20Arrival%20%5BFull%20BeatTape%5D.mp3?alt=media&token=462446bc-03b3-4af0-972d-43e6a09970f3","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2FLucid%20Green%20-%20Moonflower%20%5BFull%20EP%5D.mp3?alt=media&token=59f9f528-befc-4a70-813c-887b91ab73cd","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2FTah.%20%26%20Blumen%20-%20Slow%20Heal%20%5BFull%20BeatTape%5D.mp3?alt=media&token=e1bb10fd-4fb1-4179-b43d-2dd71b0aa179","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2FChris%20Punsalan%20-%20Since%202008%20%5BFull%20BeatTape%5D.mp3?alt=media&token=bedd4452-2b91-4775-9b95-7414b0ddd4de","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2FI'll%20be%20here%2C%20waiting%20for%20you....mp3?alt=media&token=731be18c-ef40-4663-ab7d-a0a22cc921f8","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2FMura%20Kami%20-%20Haru%20%5BFull%20BeatTape%5D.mp3?alt=media&token=5a101485-664b-4b40-b43c-8f2298555379","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2FIt%20is%20what%20it%20is...%20(lofi%20hip-hop%20mix).mp3?alt=media&token=c139a493-b60b-4d0a-ab34-4db668fc3c7a","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2FJoe%20Corfield%20-%20Roach%20Ritual%20%5BFull%20BeatTape%5D.mp3?alt=media&token=efaa7266-d4a5-4842-8d66-4241860b6746","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2FChief.%20-%20Lotus%20%5BFull%20BeatTape%5D.mp3?alt=media&token=21f976bf-d6d8-406b-80fc-576ce4b40970","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2Fyt1s.com%20-%20SEA%20%20swablu%20%20Motions%20feat%20Maberry.mp3?alt=media&token=4f312884-8acd-4a6a-964a-87f0928d0f5e","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2Fyt1s.com%20-%20Julia%20Alexa%20%20Belfa%20%20used%20to.mp3?alt=media&token=3dc7e1d8-2fa3-458c-b1b6-dc0152ace1fc","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2Fyt1s.com%20-%20Valera%20%20Everything.mp3?alt=media&token=92491624-8f26-48b6-b74b-6f95cd058f55","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2Fyt1s.com%20-%20im%20sorry.mp3?alt=media&token=41fdd037-667a-4c38-ada3-0f2dc3c537c3","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2Fyt1s.com%20-%20Teqkoi%20%20afraid%20of%20loving%20you.mp3?alt=media&token=d4d2a60f-5ab9-4662-a889-df65a1f4fd77","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood3%2Fyt1s.com%20-%20nocape%20%20bleeding%20in%20the%20battlefield%20prod%20nilowh.mp3?alt=media&token=151c4d50-4f32-4e3a-87e2-956c40b00853"};
    String[] Drive3_Audio={"Glimlip & TwoFiveOne","Maoen","Lucid Green","Tah. & Blumen","Chris Punsalan","Unknown!!!","Mura Kami","Unknown!!!","Joe Corfield","Chief.","SEA & swablu","Julia Alexa & Belfa","Valera","im6teen","Teqkoi"};
    String[] Drive3_artistname={"Five Friends","Arrival","Moonflower","Slow Heal","Since 2008","I'll be here, waiting","Haru","It is what it is...","Roach Ritual","Lotus","Motions","used to","Everything","i'm sorry","afraid of love"};
    int Drive3_images[]={R.drawable.sllep1,R.drawable.sllep2,R.drawable.sllep3,R.drawable.sllep4,R.drawable.sllep5,R.drawable.sllep6,R.drawable.sllep7,R.drawable.sllep8,R.drawable.sllep9,R.drawable.sllep10,R.drawable.sllep11,R.drawable.sllep12,R.drawable.sllep13,R.drawable.sllep14,R.drawable.sllep15,R.drawable.sllep16};

    @Override
    protected void onStart() {
        super.onStart();
         manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying())
        {

            AdforoneHour();
            Toast.makeText(this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            bottomshettplayer.setVisibility(View.INVISIBLE);
           // notificationManager.cancelAll();
        }
        else{
            isplaying=false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driveactivity3);
        shuffle=(Button)findViewById(R.id.shuffle);
        shuffle.setOnClickListener(this);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Drive_Song3.length);
        rand_int2=rand2.nextInt(Drive_Song3.length)+1;
        View  bottomsheet=findViewById(R.id.bottom_sheet_player);
        bottomshettplayer=(ConstraintLayout) findViewById(R.id.bottom_sheet_player);
        mbottomsheetBehavior= BottomSheetBehavior.from(bottomsheet);
        gaudiotext=findViewById(R.id.generalaudioname);
        gartisttext=findViewById(R.id.generalartistname);
        previous=(ImageView)findViewById(R.id.previous);
        next=(ImageView)findViewById(R.id.next);
        pause=(ImageView)findViewById(R.id.pause);
        play=(ImageView)findViewById(R.id.play);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
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
                   // onTrackPause();
                }else if (focusChange==AudioManager.AUDIOFOCUS_GAIN){
                    if (playingBeforeInterruption){
                        mediaPlayer.start();
                        updateSeekBAr();
                        play.setVisibility(View.INVISIBLE);
                        pause.setVisibility(View.VISIBLE);
                        //onTrackPlay();
                    }
                }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS){
                    mediaPlayer.pause();
                    handler.removeCallbacks(updater);
                    audioManager.abandonAudioFocus(afChangeListener);
                    play.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.VISIBLE);
                    onTrackPause();
                    audioManager.abandonAudioFocus(afChangeListener);
                }
            }
        };
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(driveactivity3.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Drive3_Audio[currentPosition].toString());
            gaudiotext.setText(Drive3_artistname[currentPosition].toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bottomshettplayer.setVisibility(View.INVISIBLE);
                //notificationManager.cancelAll();

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


        driveimage3= (ImageView) findViewById(R.id.driveimage3);
        storageReferenceDrive3= FirebaseStorage.getInstance().getReference().child("Playlist/mooded3.jpg");
       MyAdapter adapter=new MyAdapter(this,Drive3_artistname,Drive3_Audio,Drive3_images);
        ListView drivelist3=(ListView)findViewById(R.id.drivelistog3);
        drivelist3.setAdapter(adapter);
        drivelist3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Drive_Song3.length;
                        Uri uri=Uri.parse(Drive_Song3[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive3_artistname[currentPosition].toString());
                            gaudiotext.setText(Drive3_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Drive_Song3.length;
                        Uri uri=Uri.parse(Drive_Song3[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive3_artistname[currentPosition].toString());
                            gaudiotext.setText(Drive3_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=position;
                    audioname=Drive3_Audio[position];
                    name=  Drive3_artistname[position];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Drive_Song3[position]);
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Drive_Song3[position]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

               /* if (isplaying){
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
                    gartisttext.setText(Drive3_Audio[position]);
                    gaudiotext.setText(Drive3_artistname[position]);
                }else {
                    startActivity(new Intent(driveactivity3.this,nointernetactivity.class));
                }

            }

        });



        final File mostviewedimage1;
        try {
            mostviewedimage1=File.createTempFile("mooded3","jpg");
            storageReferenceDrive3.getFile(mostviewedimage1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(mostviewedimage1.getAbsolutePath());
                            ((ImageView)findViewById(R.id.driveimage3)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(driveactivity3.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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
    private void AdforoneHour(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isplaying && mInterstitialAd != null) {
                    mediaPlayer.pause();
                    prepareAD();
                    onTrackPause();
                    handler.removeCallbacks(updater);
                    pause.setVisibility(View.INVISIBLE);
                    play.setVisibility(View.VISIBLE);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                prepareAD();

            }
        }, 1000); //Timer is in ms here.

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
        tracks.add(new Track("Five Friends","Glimlip & TwoFiveOne",R.drawable.sllep1));
        tracks.add(new Track("Arrival","Maoen",R.drawable.sllep2));
        tracks.add(new Track("Moonflower","Lucid Green",R.drawable.sllep3));
        tracks.add(new Track("Slow Heal","Tah. & Blumen",R.drawable.sllep4));
        tracks.add(new Track("Since 2008","Chris Punsalan",R.drawable.sllep5));
        tracks.add(new Track("I'll be here, waiting for you...","Unknown!!!",R.drawable.sllep6));
        tracks.add(new Track("Haru","Mura Kami",R.drawable.sllep7));
        tracks.add(new Track("It is what it is...","Unknown!!!",R.drawable.sllep8));
        tracks.add(new Track("Roach Ritual","Joe Corfield",R.drawable.sllep9));
        tracks.add(new Track("Lotus","Chief.",R.drawable.sllep10));
        tracks.add(new Track("Motions","SEA & swablu",R.drawable.sllep11));
        tracks.add(new Track("used to","Julia Alexa & Belfa",R.drawable.sllep12));
        tracks.add(new Track("Everything","Valera",R.drawable.sllep13));
        tracks.add(new Track("i'm sorry","im6teen",R.drawable.sllep14));
        tracks.add(new Track("afraid of love","Teqkoi",R.drawable.sllep15));
        tracks.add(new Track("bleeding","no.cape",R.drawable.sllep16));
    }
    private void prepareAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(driveactivity3.this);
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
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    isplaying=true;
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    //onTrackPlay();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }else {
                    startActivity(new Intent(driveactivity3.this,nointernetactivity.class));
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying())
                {
                    isplaying=false;
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
                            mediaPlayer.setDataSource(Drive_Song3[rand_int2]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            shuffle.setVisibility(View.INVISIBLE);
                            // CreateNotification.createNotification(driveactivity3.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive3_Audio[rand_int2]);
                            gaudiotext.setText(Drive3_artistname[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(driveactivity3.this,nointernetactivity.class));
                    }
                   /* if (isplaying){
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
                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            shuffle.setVisibility(View.INVISIBLE);
                            mediaPlayer.setDataSource(Drive_Song3[rand_int1]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            //CreateNotification.createNotification(driveactivity3.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive3_Audio[rand_int1]);
                            gaudiotext.setText(Drive3_artistname[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(driveactivity3.this,nointernetactivity.class));
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
            }
        }
    }


    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(driveactivity3.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(driveactivity3.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
        isplaying=false;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String Drive3_Audio[];
        String Drive3_Artistname[];
        int Drive3_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.Drive3_Audio=songname;
            this.Drive3_Artistname=artistname;
            this.Drive3_images=images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listitem=layoutInflater.inflate(R.layout.tuckerlistitem,parent,false);
            ImageView imageView=listitem.findViewById(R.id.audioimage);
            songname=listitem.findViewById(R.id.tuckeraudioname);
            artistname=listitem.findViewById(R.id.tuckeraudioartistname);


            //imageView.setImageResource(Mostviewed1_images[position]);
            songname.setText(Drive3_Audio[position].toString());
            imageView.setImageResource(Drive3_images[position]);
            artistname.setText(Drive3_Artistname[position].toString());
            return listitem;

        }
    }
}