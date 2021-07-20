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

public class mostviewed1 extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView mostviewimage1;
    int currentPosition;
    List<Track> tracks;
    NotificationManager notificationManager;
    private BottomSheetBehavior mbottomsheetBehavior;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "viewed1";
    SeekBar Scrubber;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    SeekBar VolumeControl;
    private Handler handler=new Handler();
    int MaxVolume;
    int CurVolume;
    ImageView play;
    ImageView pause;
    ImageView previous;
    ImageView next;
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
    public boolean isplaying=false;

    Button shuffle;
    private StorageReference storageReferenceMostviewed1;
    TextView songname;
    TextView artistname;
    String[] Viewed_Song1={"https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20We%20Are%20One.mp3?alt=media&token=def89858-3ba7-4232-8762-fd0f2d8f9f95","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Upbeat%20Party.mp3?alt=media&token=ee6a8147-08e8-4bae-9fd0-0242627b55e9","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Stylish%20Groove.mp3?alt=media&token=1c2822d6-195b-4f18-8386-0c252c96054e","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Stylish%20Groove.mp3?alt=media&token=1c2822d6-195b-4f18-8386-0c252c96054e","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Stomps%20and%20Claps.mp3?alt=media&token=2c65a098-0ee1-4473-a362-08ba12e81c10","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Stomps%20and%20Claps.mp3?alt=media&token=2c65a098-0ee1-4473-a362-08ba12e81c10","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Space%20Orbit.mp3?alt=media&token=aa069e6c-f983-4d0e-bba7-0d81c1dc9ede","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Smart%20Tech.mp3?alt=media&token=3128c745-1907-4c01-aaa2-0ed410dc77b1","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Reflections.mp3?alt=media&token=f32288e5-0c84-4b5b-825e-71d04c796063","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Positive%20Energy.mp3?alt=media&token=65f50184-4c9e-4e59-9521-fb88d7e5b87a","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Little%20Idea.mp3?alt=media&token=05086a89-f2d0-42a8-82e0-80375706a881","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Hotshot.mp3?alt=media&token=a9a14d49-f562-49e9-b4f0-8e9617a42be0","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Future%20Bass.mp3?alt=media&token=f168c529-ed08-46d1-9e7f-c76ff3a6fbdd","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Energy.mp3?alt=media&token=e1c2ad4d-a93c-4e41-8978-47a034830696","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Energy.mp3?alt=media&token=e1c2ad4d-a93c-4e41-8978-47a034830696","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FScott%20Holmes%20Music%20-%20Come%20and%20Get%20It.mp3?alt=media&token=7e2ffc12-a375-41d6-9ee3-0359ec08ebf0","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2Fyt1s.com%20-%20%EF%BC%AC%EF%BC%A9%EF%BC%A6%EF%BC%A5.mp3?alt=media&token=e0715d93-b190-4552-af51-dad42f6f2da2","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2Fyt1s.com%20-%20%EF%BC%B2%EF%BC%A5%EF%BC%A1%EF%BC%A3%EF%BC%A8%EF%BC%A9%EF%BC%AE%EF%BC%A7.mp3?alt=media&token=37755b9b-6b5b-4a8a-8fa7-ada072e70e32"};
    String[] Mostviewed1_Audio={"We are one","Upbeat Party","Stylish Groove","Stomps and Claps","Space Orbit","Smart Tech"," Reflections","Positive Energy","Little Idea","Hotshot","Future Bass","Energy","Dream Come True","Come and Get It","ＬＩＦＥ","ＲＥＡＣＨＩＮＧ"};
    String[] Mostviewed1_artistname={"Scott Holmes Music"};
    int Mostviewed1_images[]={R.drawable.mv1,R.drawable.mv2,R.drawable.mv3,R.drawable.mv4,R.drawable.mv5,R.drawable.mv6,R.drawable.mv7,R.drawable.mv8,R.drawable.mv9,R.drawable.mv10,R.drawable.mv11,R.drawable.mv12,R.drawable.mv13,R.drawable.mv14,R.drawable.mv15,R.drawable.mv16};

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostviewed1);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Mostviewed1_Audio.length);
        rand_int2=rand2.nextInt(Mostviewed1_artistname.length)+1;
        shuffle=(Button)findViewById(R.id.shuffle);
        shuffle.setOnClickListener(this);
        View  bottomsheet=findViewById(R.id.bottom_sheet_player);
        bottomshettplayer=(ConstraintLayout) findViewById(R.id.bottom_sheet_player);
        mbottomsheetBehavior=BottomSheetBehavior.from(bottomsheet);
        gaudiotext=findViewById(R.id.generalaudioname);
        gartisttext=findViewById(R.id.generalartistname);
        pause=(ImageView)findViewById(R.id.pause);
        play=(ImageView)findViewById(R.id.play);
        previous=(ImageView)findViewById(R.id.previous);
        next=(ImageView)findViewById(R.id.next);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        VolumeControl=(SeekBar)findViewById(R.id.seekBarVolume);
        Scrubber=(SeekBar)findViewById(R.id.seekBarScrubber);
        mediaPlayer=new MediaPlayer();
        intentFilter=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        noisyAudioStreamreceiver=new NoisyAudioStreamreceiver();
        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        MaxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        CurVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
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
                        //onTrackPlay();
                    }
                }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS){
                    mediaPlayer.pause();
                    handler.removeCallbacks(updater);
                    audioManager.abandonAudioFocus(afChangeListener);
                    play.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.VISIBLE);
                    //onTrackPause();
                    audioManager.abandonAudioFocus(afChangeListener);
                }
            }
        };
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(mostviewed1.this,"ca-app-pub-2470688793006998/8361798413", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Mostviewed1_artistname[0].toString());
            gaudiotext.setText(Mostviewed1_Audio[currentPosition].toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bottomshettplayer.setVisibility(View.INVISIBLE);
                //notificationManager.cancelAll();
                //prepareAD();
                //shuffle.setVisibility(View.VISIBLE);
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



        mostviewimage1= (ImageView) findViewById(R.id.mostviewimage1);
        storageReferenceMostviewed1= FirebaseStorage.getInstance().getReference().child("Playlist/mostv1.jpg");
        MyAdapter adapter=new MyAdapter(this,Mostviewed1_Audio,Mostviewed1_artistname,Mostviewed1_images);
        ListView mostviewedaudiolist1=(ListView)findViewById(R.id.mostviewedlistog);
        mostviewedaudiolist1.setAdapter(adapter);
        mostviewedaudiolist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Viewed_Song1.length;
                        Uri uri=Uri.parse(Viewed_Song1[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed1_artistname[currentPosition].toString());
                            gaudiotext.setText(Mostviewed1_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Viewed_Song1.length;
                        Uri uri=Uri.parse(Viewed_Song1[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed1_artistname[currentPosition].toString());
                            gaudiotext.setText(Mostviewed1_Audio[currentPosition].toString());
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
                    audioname=Mostviewed1_Audio[position];
                    name=  Mostviewed1_artistname[0];
                    bottomshettplayer.setVisibility(View.VISIBLE);

                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Viewed_Song1[position]);
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Viewed_Song1[position]);
                            mediaPlayer.prepare();
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
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    gartisttext.setText(Mostviewed1_artistname[0]);
                    gaudiotext.setText(Mostviewed1_Audio[position]);

                }else {
                    startActivity(new Intent(mostviewed1.this,nointernetactivity.class));
                }
            }

        });




        final File mostviewedimage1;
        try {
            mostviewedimage1=File.createTempFile("mostv1","jpg");
            storageReferenceMostviewed1.getFile(mostviewedimage1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(mostviewedimage1.getAbsolutePath());
                            ((ImageView)findViewById(R.id.mostviewimage1)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mostviewed1.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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
   /* private void createChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Decibel Inc", NotificationManager.IMPORTANCE_LOW);
            notificationManager=getSystemService(NotificationManager.class);
            if (notificationManager !=null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }*/
    private void prepareAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(mostviewed1.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

    }

    private void populatetrack() {
        tracks=new ArrayList<>();
        tracks.add(new Track("We are one","Scott Holmes Music",R.drawable.mv1));
        tracks.add(new Track("Upbeat Party","Scott Holmes Music",R.drawable.mv2));
        tracks.add(new Track("Stylish Groove","Scott Holmes Music",R.drawable.mv3));
        tracks.add(new Track("Stomps and Claps","Scott Holmes Music",R.drawable.mv4));
        tracks.add(new Track("Space Orbit","Scott Holmes Music",R.drawable.mv5));
        tracks.add(new Track("Smart Tech","Scott Holmes Music",R.drawable.mv6));
        tracks.add(new Track(" Reflections","Scott Holmes Music",R.drawable.mv7));
        tracks.add(new Track("Positive Energy","Scott Holmes Music",R.drawable.mv8));
        tracks.add(new Track("Little Idea","Scott Holmes Music",R.drawable.mv9));
        tracks.add(new Track("Hotshot","Scott Holmes Music",R.drawable.mv10));
        tracks.add(new Track("Future Bass","Scott Holmes Music",R.drawable.mv11));
        tracks.add(new Track("Energy","Scott Holmes Music",R.drawable.mv12));
        tracks.add(new Track("Dream Come True","Scott Holmes Music",R.drawable.mv13));
        tracks.add(new Track("Come and Get It","Scott Holmes Music",R.drawable.mv14));
        tracks.add(new Track("ＬＩＦＥ","Scott Holmes Music",R.drawable.mv15));
        tracks.add(new Track("ＲＥＡＣＨＩＮＧ","Scott Holmes Music",R.drawable.mv16));
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
                    startActivity(new Intent(mostviewed1.this,nointernetactivity.class));
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying())
                {
                    unregisterReceiver(noisyAudioStreamreceiver);
                   // onTrackPause();
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
                            mediaPlayer.setDataSource(Viewed_Song1[rand_int2]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            shuffle.setVisibility(View.INVISIBLE);
                            // CreateNotification.createNotification(mostviewed1.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed1_artistname[0]);
                            gaudiotext.setText(Mostviewed1_Audio[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(mostviewed1.this,nointernetactivity.class));
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
                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            shuffle.setVisibility(View.INVISIBLE);
                            mediaPlayer.setDataSource(Viewed_Song1[rand_int1]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            // CreateNotification.createNotification(mostviewed1.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed1_artistname[0]);
                            gaudiotext.setText(Mostviewed1_Audio[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(mostviewed1.this,nointernetactivity.class));
                    }
                 /*   if (isplaying){
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
                onTrackPause();
                handler.removeCallbacks(updater);
            }
            else if (AudioManager.ACTION_HEADSET_PLUG.equals(intent.getAction())){
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
        CreateNotification.createNotification(mostviewed1.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(mostviewed1.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
        String Mostviewed1_Audio[];
        String Mostviewed1_Artistname[];
        int Mostviewed1_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.Mostviewed1_Audio=songname;
            this.Mostviewed1_Artistname=artistname;
            this.Mostviewed1_images=images;
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
            songname.setText(Mostviewed1_Audio[position].toString());
            imageView.setImageResource(Mostviewed1_images[position]);
            artistname.setText(Mostviewed1_Artistname[0].toString());
            return listitem;

        }
    }
}