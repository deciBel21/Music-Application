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

public class driveactivity4 extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView driveimage4;
    private BottomSheetBehavior mbottomsheetBehavior;
    ImageView play;
    List<Track> tracks;
    private InterstitialAd mInterstitialAd;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    private static final String TAG = "drive4";
    NotificationManager notificationManager;
    int currentPosition;
    Random rand2;
    ImageView pause;
    SeekBar Scrubber;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    SeekBar VolumeControl;
    ImageView previous;
    ImageView next;
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
    public boolean isplaying=false;
    int CurVolume;
    ConnectivityManager manager;

    Button shuffle;
    private StorageReference storageReferenceDrive4;
    TextView songname;

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
    }

    TextView artistname;
    String[] Drive_Song4={"https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fmidnight%20studying.%20%5Blofijazzhopchill%20mix%5D.mp3?alt=media&token=21e79060-7bcc-4f9d-8c01-e24845f4f7f3","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fwalking%20in%20the%20rain%20sad%20lofi%20mix.mp3?alt=media&token=0ab3bcc5-1be8-49f0-9fad-919f15a4f7f8","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fsad%20lofi%20for%20staying%20inside.mp3?alt=media&token=fed5d4a8-5673-4736-bbc3-44223f125e1d","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fi%20made%20this%20tape%20for%20you%20last%20night.mp3?alt=media&token=8b701d17-64f7-40d5-9144-9feaeaa43351","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2FDillan%20Witherow%20-%20Before%20Sunrise%20%5Blofi%20hip%20hoprelaxing%20beats%5D.mp3?alt=media&token=07b1fc42-b3a0-4499-a43d-2d4f473299e5","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2FCelestial%20Alignment%20-%20Precious%20Moments%20%5Blofi%20hip%20hoprelaxing%20beats%5D.mp3?alt=media&token=ac4008e2-31be-4345-b147-9d7e5ede7641","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2FBVG%20-%20Temple%20Garden%20-%20%5Basian%20lofi%20hip%20hoprelaxing%20beats%5D.mp3?alt=media&token=55c4ad2b-fe21-4060-9d72-c28219555f84","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Ffourwalls%20-%20Staring%20Contest%20%5Blofi%20hip%20hoprelaxing%20beats%5D.mp3?alt=media&token=9abf47ab-166d-49f1-90b9-2beed331f349","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2FKanisan%20-%20R%C3%BCya%20%5Blofi%20hip%20hoprelaxing%20beats%5D.mp3?alt=media&token=a6f9401f-73e0-4e82-9799-8ac10019f92d","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fyt1s.com%20-%20S%C3%B8lace%20%20I%20Still%20See%20Your%20Face.mp3?alt=media&token=de39c21d-7b96-46a0-ad4f-297a31131bb5","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fyt1s.com%20-%20wander%20all%20winter%20%20Im%20Here%20feat%20Jeannine%20Fontyn.mp3?alt=media&token=cea4f368-36ee-47d2-8211-1e94208c3eae","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fyt1s.com%20-%20yaeow%20%20To%20Make%20It%20Right.mp3?alt=media&token=dac99c7c-e8e3-407e-a3ed-77cddadad34c","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fyt1s.com%20-%20wxse%20%20I%20Feel%20So%20Empty%20ft%20Lul%20Patchy.mp3?alt=media&token=48c9ad1e-ccbd-4729-8ac3-701f1f2c5c20","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fyt1s.com%20-%20fairway%20%20hide%20ft%20Julia%20Alexa.mp3?alt=media&token=eeaa8d0c-ace0-4063-8028-f612b44b7aa5","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fyt1s.com%20-%20SEA%20%20Moving%20On%20ft%20Fab%20Morris.mp3?alt=media&token=414aab98-b710-4c8f-9c6e-902762e3eb9b","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood4%2Fyt1s.com%20-%20Zaini%20%20Joke%20ft%20Vict%20Molina%20%20Keagan.mp3?alt=media&token=89c895ba-830a-409c-b66d-9b4976890360"};
    String[] Drive4_Audio={"lofijazz","Unknown!!!","Sad","MIX","Dillan Witherow","Celestial Alignment","BVG","fourwalls","Kanisan","Sølace","wander all winter","yaeow","wxse","fairway ft.Alexa","SEA ft.Fab Morris","Joke ft.Vict Molina & Keagan"};
    String[] Drive4_artistname={"midnight studying.","walking in the rain"," lofi for staying inside","i made this tape","Before Sunrise","Precious Moments","Temple Garden","Staring Contest","Rüya"," I Still See Your Face","I'm Here","To Make It Right","I Feel So Empty","hide"," Moving On","Zaini"};
    int Drive4_images[]={R.drawable.study1,R.drawable.study2,R.drawable.study3,R.drawable.study4,R.drawable.study5,R.drawable.study6,R.drawable.study7,R.drawable.study8,R.drawable.study9,R.drawable.study10,R.drawable.study11,R.drawable.study12,R.drawable.study13,R.drawable.study14,R.drawable.study15,R.drawable.study16};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driveactivity4);
        shuffle=(Button)findViewById(R.id.shuffle);
        shuffle.setOnClickListener(this);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Drive_Song4.length);
        rand_int2=rand2.nextInt(Drive_Song4.length)+1;
        previous=(ImageView)findViewById(R.id.previous);
        next=(ImageView)findViewById(R.id.next);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        View  bottomsheet=findViewById(R.id.bottom_sheet_player);
        bottomshettplayer=(ConstraintLayout) findViewById(R.id.bottom_sheet_player);
        mbottomsheetBehavior= BottomSheetBehavior.from(bottomsheet);
        gaudiotext=findViewById(R.id.generalaudioname);
        gartisttext=findViewById(R.id.generalartistname);
        pause=(ImageView)findViewById(R.id.pause);
        play=(ImageView)findViewById(R.id.play);
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
                       // onTrackPlay();
                    }
                }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS){
                    mediaPlayer.pause();
                    handler.removeCallbacks(updater);
                    audioManager.abandonAudioFocus(afChangeListener);
                    play.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.VISIBLE);
                  //  onTrackPause();
                    audioManager.abandonAudioFocus(afChangeListener);
                }
            }
        };
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(driveactivity4.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Drive4_Audio[currentPosition].toString());
            gaudiotext.setText(Drive4_artistname[currentPosition].toString());
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


        driveimage4= (ImageView) findViewById(R.id.driveimage4);
        storageReferenceDrive4= FirebaseStorage.getInstance().getReference().child("Playlist/mooded4.jpg");
        MyAdapter adapter=new MyAdapter(this, Drive4_artistname,Drive4_Audio,Drive4_images);
        ListView drivelist4=(ListView)findViewById(R.id.drivelistog4);
        drivelist4.setAdapter(adapter);
        drivelist4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Drive_Song4.length;
                        Uri uri=Uri.parse(Drive_Song4[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive4_artistname[currentPosition].toString());
                            gaudiotext.setText(Drive4_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Drive_Song4.length;
                        Uri uri=Uri.parse(Drive_Song4[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive4_artistname[currentPosition].toString());
                            gaudiotext.setText(Drive4_Audio[currentPosition].toString());
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
                    audioname=Drive4_Audio[position];
                    name=  Drive4_artistname[position];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Drive_Song4[position]);
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Drive_Song4[position]);
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
                    gartisttext.setText(Drive4_Audio[position]);
                    gaudiotext.setText(Drive4_artistname[position]);
                }else {
                    startActivity(new Intent(driveactivity4.this,nointernetactivity.class));
                }

            }

        });



        final File mostviewedimage1;
        try {
            mostviewedimage1=File.createTempFile("mooded4","jpg");
            storageReferenceDrive4.getFile(mostviewedimage1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(mostviewedimage1.getAbsolutePath());
                            ((ImageView)findViewById(R.id.driveimage4)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(driveactivity4.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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
    /*private void createChannel() {
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
        tracks.add(new Track("midnight studying.","lofijazz",R.drawable.study1));
        tracks.add(new Track("walking in the rain","Unknown!!!",R.drawable.study2));
        tracks.add(new Track("lofi for staying inside","Sad",R.drawable.study3));
        tracks.add(new Track("i made this tape for you last night","MIX",R.drawable.study4));
        tracks.add(new Track("Before Sunrise","Dillan Witherow",R.drawable.study5));
        tracks.add(new Track("Precious Moments","Celestial Alignment",R.drawable.study6));
        tracks.add(new Track("Temple Garden","BVG",R.drawable.study7));
        tracks.add(new Track("Staring Contest","fourwalls",R.drawable.study8));
        tracks.add(new Track("Rüya","Kanisan",R.drawable.study9));
        tracks.add(new Track("I Still See Your Face","Sølace",R.drawable.study10));
        tracks.add(new Track("I'm Here","wander all winter",R.drawable.study11));
        tracks.add(new Track("To Make It Right","yaeow",R.drawable.study12));
        tracks.add(new Track("I Feel So Empty","wxse",R.drawable.study13));
        tracks.add(new Track("hide","fairway ft.Alexa",R.drawable.study14));
        tracks.add(new Track("Moving On","SEA ft. Fab Morris",R.drawable.study15));
        tracks.add(new Track("Zaini","Joke ft.Vict Molina & Keagan",R.drawable.study16));
    }
    private void prepareAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(driveactivity4.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

    }
    private void AdforoneHour(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isplaying && mInterstitialAd != null) {
                    mediaPlayer.pause();
                    prepareAD();
                   // onTrackPause();
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
                    startActivity(new Intent(driveactivity4.this,nointernetactivity.class));
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
                            mediaPlayer.setDataSource(Drive_Song4[rand_int2]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            shuffle.setVisibility(View.INVISIBLE);
                            //CreateNotification.createNotification(driveactivity4.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive4_Audio[rand_int2]);
                            gaudiotext.setText(Drive4_artistname[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(driveactivity4.this,nointernetactivity.class));
                    }


                  /*  if (isplaying){
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
                            mediaPlayer.setDataSource(Drive_Song4[rand_int1]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            // CreateNotification.createNotification(driveactivity4.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive4_Audio[rand_int1]);
                            gaudiotext.setText(Drive4_artistname[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(driveactivity4.this,nointernetactivity.class));
                    }
                    /*if (isplaying){
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
                handler.removeCallbacks(updater);
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
             notificationManager.cancelAll();
            }
        }
    }

    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(driveactivity4.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
        mediaPlayer.pause();
        updateSeekBAr();
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.VISIBLE);
        isplaying=true;

    }

    @Override
    public void onTrackPause() {
        if(mediaPlayer.isPlaying())
        {
            CreateNotification.createNotification(driveactivity4.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
        String Drive4_Audio[];
        String Drive4_Artistname[];
        int Drive4_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.Drive4_Audio=songname;
            this.Drive4_Artistname=artistname;
            this.Drive4_images=images;
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
            songname.setText(Drive4_Audio[position].toString());
            imageView.setImageResource(Drive4_images[position]);
            artistname.setText(Drive4_Artistname[position].toString());
            return listitem;

        }
    }
}