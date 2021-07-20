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

public class driveactivity1 extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView driveimage1;
    int currentPosition;
    List<Track> tracks;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "drive1";
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    private BottomSheetBehavior mbottomsheetBehavior;
    NotificationManager notificationManager;
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
    ConnectivityManager manager;

    Button shuffle;
    private StorageReference storageReferenceDrive1;
    TextView songname;
    TextView artistname;
    ArrayList<String> songs=new ArrayList<String>();
    String[] Drive_Song1={"https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood1%2Fvideoplayback.mp3?alt=media&token=05d49a3a-aa97-444c-aee1-5ecbede2ca60","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood1%2Fvideoplayback%20(1).mp3?alt=media&token=2624edec-74e7-49a3-8486-bed7f0fc3dde","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood1%2Fvideoplayback%20(2).mp3?alt=media&token=0dff61d3-9b3c-40bd-b500-675eb850107f","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood1%2Fvideoplayback%20(4).mp3?alt=media&token=d86c2b5b-d778-4ac8-89b9-c6d9afe63a7f","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood1%2Fvideoplayback%20(5).mp3?alt=media&token=b8788abe-32b7-4876-ae5f-f8e98abd7594","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood1%2Fvideoplayback%20(6).mp3?alt=media&token=a7adb32b-691d-4384-b619-986057632a81","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood1%2Fvideoplayback%20(7).mp3?alt=media&token=ff21e13a-cee4-42c1-8a28-7c82da08d785","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood1%2Fvideoplayback%20(8).mp3?alt=media&token=286124e2-5adf-4048-85d6-ca7840043194","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood1%2Fvideoplayback%20(9).mp3?alt=media&token=493774b7-7625-4dc6-964c-96b22a963311","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20Misha%20x%20Jussi%20Halme%20%20Bliss%20chill%20lofi%20beats.mp3?alt=media&token=63aa17b7-965f-4b50-b8bd-20f8dccd840e","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20Ezzy%20%20ny90%20Chillhop%20Essentials%20Fall%202020.mp3?alt=media&token=1b0dcce0-6d3e-420f-b81c-afc9e684f95a","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20Montell%20Fish%20%20Imagination%20Chillhop%20Essentials%20Fall%202020.mp3?alt=media&token=2b9e8833-0741-4c7b-88f4-da73e68574f3","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20Kendall%20Miles%20%20H%20E%20R%20B%20%20Paraglider%20chill%20instrumental%20beats.mp3?alt=media&token=dfecd5c1-ec6d-4869-9282-6681b457bb6f","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20Clap%20Cotton%20%20Vinho%20Verde%20instrumental%20chill%20beats.mp3?alt=media&token=d50614e5-cb97-4110-8173-64ca45143077","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20Mo%20Anando%20%20Yesterday%20instrumental%20beats.mp3?alt=media&token=7ffafec5-75c5-4c0a-a791-73287bb36604","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20%C3%98DYSSEE%20x%20Florent%20Garcia%20%20Calm%20relaxing%20chillhop%20beat.mp3?alt=media&token=651880f8-6ad6-417c-9580-5986d1d86580"};
    String[] Drive1_Audio={"Swablu","wOOds & Roiael","yaeow","maberry","imfinewnow ft.Solace","Rnla & SEA","Kaxi,Teqkoi & enluv","madson","dyslm ft.Resident","Misha x Jussi Halme","Ezzy","Montell Fish","Kendall Miles & H E R B ","Clap Cotton","Mo Anando","ØDYSSEE x Florent Garcia"};
    String[] Drive1_artistname={"In your Eyes","Cry me a River","I need you","Still think of you","Just a mistake","Love Addict","I don't want to hurt","Changes","Losing my Chances!","Bliss","ny90"," Imagination","Paraglider","Vinho Verde","Yesterday","Calm"};
    int Drive1_images[]={R.drawable.drive10,R.drawable.drive11,R.drawable.drive4,R.drawable.drive5,R.drawable.drive12,R.drawable.drived3,R.drawable.drived4,R.drawable.drive13,R.drawable.drived6,R.drawable.drive2,R.drawable.drive14,R.drawable.drive16,R.drawable.drive12,R.drawable.drive11,R.drawable.drived5,R.drawable.drive17};

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
          prepareAD();
            Toast.makeText(this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            handler.removeCallbacks(updater);
            bottomshettplayer.setVisibility(View.INVISIBLE);
            //notificationManager.cancelAll();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driveactivity1);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Drive_Song1.length);
        rand_int2=rand2.nextInt(Drive_Song1.length)+1;
        shuffle=(Button)findViewById(R.id.shuffle);
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
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        VolumeControl=(SeekBar)findViewById(R.id.seekBarVolume);
        Scrubber=(SeekBar)findViewById(R.id.seekBarScrubber);
        mediaPlayer=new MediaPlayer();
        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        MaxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        intentFilter=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        noisyAudioStreamreceiver=new NoisyAudioStreamreceiver();
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
                   // onTrackPause();
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
                   // onTrackPause();
                    audioManager.abandonAudioFocus(afChangeListener);
                }
            }
        };
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(driveactivity1.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Drive1_Audio[currentPosition].toString());
            gaudiotext.setText(Drive1_artistname[currentPosition].toString());
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


        driveimage1= (ImageView) findViewById(R.id.driveimage1);
storageReferenceDrive1= FirebaseStorage.getInstance().getReference().child("Playlist/drived1.jpg");
        MyAdapter adapter=new MyAdapter(this,Drive1_artistname,Drive1_Audio,Drive1_images);
        ListView drivelist1=(ListView)findViewById(R.id.drivelistog);
        drivelist1.setAdapter(adapter);
        drivelist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%songs.size();
                        Uri uri=Uri.parse(songs.get(currentPosition).toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive1_artistname[currentPosition].toString());
                            gaudiotext.setText(Drive1_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%songs.size();
                        Uri uri=Uri.parse(songs.get(currentPosition).toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive1_artistname[currentPosition].toString());
                            gaudiotext.setText(Drive1_Audio[currentPosition].toString());
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
                    audioname=Drive1_Audio[position];
                    name=  Drive1_artistname[position];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Drive_Song1[position]);
                            mediaPlayer.prepare();

                        }
                        else
                        { mediaPlayer.reset();
                            mediaPlayer.setDataSource(Drive_Song1[position]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                /*if (isplaying){
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
                    gartisttext.setText(Drive1_Audio[position]);
                    gaudiotext.setText(Drive1_artistname[position]);
                }else {
                    startActivity(new Intent(driveactivity1.this,nointernetactivity.class));
                }

            }

        });






        final File mostviewedimage1;
        try {
            mostviewedimage1=File.createTempFile("drived1","png");
            storageReferenceDrive1.getFile(mostviewedimage1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(mostviewedimage1.getAbsolutePath());
                            ((ImageView)findViewById(R.id.driveimage1)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(driveactivity1.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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
        tracks.add(new Track("In your eyes","Swablu",R.drawable.drive10));
        tracks.add(new Track("Cry me a River","wOODs & Roiael",R.drawable.drive11));
        tracks.add(new Track("I need you","yaeow",R.drawable.drive4));
        tracks.add(new Track("I still think of you","Maberry",R.drawable.drive5));
        tracks.add(new Track("Just a mistake","Imfinenow ft.Solace",R.drawable.drive12));
        tracks.add(new Track("Love addict","Rnla & SEA",R.drawable.drived3));
        tracks.add(new Track("I don't want to hurt you","Kaxi,Teqkoi & enluv",R.drawable.drived4));
        tracks.add(new Track("Changes","Madson",R.drawable.drive13));
        tracks.add(new Track("Losing my Chances","Dyslm",R.drawable.drived6));
        tracks.add(new Track("Bliss","Misha x Jussi Halme",R.drawable.drive2));
        tracks.add(new Track("ny90","Ezzy",R.drawable.drive14));
        tracks.add(new Track("Imagination","Montell Fish",R.drawable.drive16));
        tracks.add(new Track("Paraglider","Kendall Miles & H E R B",R.drawable.drive12));
        tracks.add(new Track("Vinho Verde","Clap Cotton",R.drawable.drive11));
        tracks.add(new Track("Yesterday","Mo Anando",R.drawable.drived5));
        tracks.add(new Track("Calm","ØDYSSEE x Florent Garcia",R.drawable.drive17));

    }


    private Runnable updater= new Runnable() {
        @Override
        public void run() {
            updateSeekBAr();
            long currentduration=mediaPlayer.getCurrentPosition();
        }
    };
    private void prepareAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(driveactivity1.this);
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
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    //onTrackPlay();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }else {
                    startActivity(new Intent(driveactivity1.this,nointernetactivity.class));
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
                            mediaPlayer.setDataSource(Drive_Song1[rand_int2]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            shuffle.setVisibility(View.INVISIBLE);
                            //CreateNotification.createNotification(driveactivity1.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive1_Audio[rand_int2]);
                            gaudiotext.setText(Drive1_artistname[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(driveactivity1.this,nointernetactivity.class));
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
                            mediaPlayer.setDataSource(Drive_Song1[rand_int1]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            // CreateNotification.createNotification(driveactivity1.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive1_Audio[rand_int1]);
                            gaudiotext.setText(Drive1_artistname[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(driveactivity1.this,nointernetactivity.class));
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
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
                notificationManager.cancelAll();
            }
        }
    }


    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(driveactivity1.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
        mediaPlayer.start();
        updateSeekBAr();
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.VISIBLE);
        isplaying=true;
    }

    @Override
    public void onTrackPause() {

            CreateNotification.createNotification(driveactivity1.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
            mediaPlayer.pause();
            handler.removeCallbacks(updater);
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
            isplaying=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // notificationManager.cancelAll();
        unregisterReceiver(broadcastReceiver);
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String Drive1_Audio[];
        String Drive1_Artistname[];
        int Drive1_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.Drive1_Audio=songname;
            this.Drive1_Artistname=artistname;
            this.Drive1_images=images;
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
            songname.setText(Drive1_Audio[position].toString());
            artistname.setText(Drive1_Artistname[position].toString());
            imageView.setImageResource(Drive1_images[position]);
            return listitem;

        }
    }
    }
