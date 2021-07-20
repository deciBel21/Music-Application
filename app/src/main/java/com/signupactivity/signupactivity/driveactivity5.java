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

public class driveactivity5 extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView driveimage5;
    int currentPosition;
    List<Track> tracks;
    NotificationManager notificationManager;
    private BottomSheetBehavior mbottomsheetBehavior;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "drive5";
    ImageView play;
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
    int CurVolume;
    public boolean isplaying=false;
    ConnectivityManager manager;

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

    Button shuffle;
    private StorageReference storageReferenceDrive5;
    TextView songname;
    TextView artistname;
    String[] Drive_Song5={"https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2FManhattan%20JazzHop.mp3?alt=media&token=5b1c1f14-2b6f-484d-b6e0-b6d19617f0a8","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2FUpbeat%20Funk%20Lofi%20HipHop%20Mix.mp3?alt=media&token=5dfd63b5-beeb-4ae8-ac3f-dbd09a92582a","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2FSunday%20Joyride%20(Upbeat%20Lo-fi%20Hip%20Hop%20Mix).mp3?alt=media&token=2fbd9766-537b-4729-b661-8d920aca1372","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2Fold%20songs%20but%20it's%20lofi%20remix.mp3?alt=media&token=29fec43d-3f7b-4f21-9728-442372b76399","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2Fchromonicci%20-%20Dreamworlds.%20%5Bdreamy%20instrumental%20beats%5D.mp3?alt=media&token=dfb885c4-92b6-43a7-93be-686ddbdb17f4","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2FWard%20Wills%20-%20When%20to%20Say%20Goodbye%20%5Bchill%20lofi%20beats%5D.mp3?alt=media&token=02bcbbf8-0f79-40a1-b9e3-fed2460d005a","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2FManhattan%20JazzHop.mp3?alt=media&token=5b1c1f14-2b6f-484d-b6e0-b6d19617f0a8","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2Flate%20night%20vibes....mp3?alt=media&token=13532beb-4834-42d9-bc37-61376d902f49","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2Flofi%20songs%20for%20slow%20days.mp3?alt=media&token=7323ab93-69c3-46de-b01d-a8e6c07a68b3","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2FJust%20wanna%20stay%20here%20forever%20lofi%20hip%20hop%20mix.mp3?alt=media&token=efe4ce82-753e-4438-aaa6-f12ac79b9891","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2Fyt1s.com%20-%20TYRELLSKRT%20%20O%20DOG%20prod%20NOXYGEN.mp3?alt=media&token=7d4ef32c-38a9-4068-9047-b115986c1e5f","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2Fyt1s.com%20-%20WETT%20BRAiN%20%20Bulimia%20ft%20ITSOKTOCRY.mp3?alt=media&token=255357bc-e293-4ed1-86ee-eb83088f002d","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2Fyt1s.com%20-%20BLUPILL%20%20DROPKICK.mp3?alt=media&token=02e120b2-71ca-4bd8-91ef-48e70a183d6e","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2Fyt1s.com%20-%20Wavele%20%20Comet.mp3?alt=media&token=0b022be3-8e5e-4ab5-a0f1-bb7396a766bb","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2Fyt1s.com%20-%20Kamiyada%20%20Metal%20In%20Me.mp3?alt=media&token=bb6e0c60-00ca-4b87-bfb6-40ffc328b9db","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Mood5%2Fyt1s.com%20-%20Kasper%20Fell%20%20Crying%20out%20prod%20Yago.mp3?alt=media&token=28953a20-d943-4b59-b52e-69988a9978b9"};
    String[] Drive5_Audio={"Jazz Hop","Upbeat","Sunday","EMix","chromonicci ","Ward Wills","Manhattan","Unknown!!!","slow69","MIX","TYRELLSKRT prod. NOXYGEN","WETT BRAiN ft.ITSOKTOCRY","DROPKICK","Wavele$$","Kamiyada+","Kasper Fell prod. Yago"};
    String[] Drive5_artistname={"Swing Beats","Funk Lofi HipHop Mix","Joyride (Upbeat Lo-fi)","old songs but it's lofi ","Dreamworlds.","When to Say Goodbye","JazzHop","late night vibes....mp3","lofi songs days","Just wanna stay here.","O DOG","Bulimia","BLUPILL","Comet","Metal In Me","Crying out"};
    int Drive5_images[]={R.drawable.party1,R.drawable.party2,R.drawable.party3,R.drawable.party4,R.drawable.party5,R.drawable.party6,R.drawable.party7,R.drawable.party8,R.drawable.party9,R.drawable.party10,R.drawable.party11,R.drawable.party12,R.drawable.party13,R.drawable.party14,R.drawable.party15,R.drawable.party16};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driveactivity5);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Drive_Song5.length);
        rand_int2=rand2.nextInt(Drive_Song5.length)+1;
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
        InterstitialAd.load(driveactivity5.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Drive5_Audio[currentPosition].toString());
            gaudiotext.setText(Drive5_artistname[currentPosition].toString());
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


        driveimage5= (ImageView) findViewById(R.id.driveimage5);
        storageReferenceDrive5= FirebaseStorage.getInstance().getReference().child("Playlist/mooded5.jpg");
        MyAdapter adapter=new MyAdapter(this,Drive5_artistname,Drive5_Audio,Drive5_images);
        ListView drivelist5=(ListView)findViewById(R.id.drivelistog5);
        drivelist5.setAdapter(adapter);
        drivelist5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Drive_Song5.length;
                        Uri uri=Uri.parse(Drive_Song5[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive5_artistname[currentPosition].toString());
                            gaudiotext.setText(Drive5_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Drive_Song5.length;
                        Uri uri=Uri.parse(Drive_Song5[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive5_artistname[currentPosition].toString());
                            gaudiotext.setText(Drive5_Audio[currentPosition].toString());
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
                    audioname=Drive5_Audio[position];
                    name=  Drive5_artistname[position];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Drive_Song5[position]);
                            mediaPlayer.prepare();

                        }
                        else
                        {   mediaPlayer.reset();
                            mediaPlayer.setDataSource(Drive_Song5[position]);
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
                    gartisttext.setText(Drive5_Audio[position]);
                    gaudiotext.setText(Drive5_artistname[position]);
                }else {
                    startActivity(new Intent(driveactivity5.this,nointernetactivity.class));
                }

            }

        });




        final File mostviewedimage1;
        try {
            mostviewedimage1=File.createTempFile("mooded5","jpg");
            storageReferenceDrive5.getFile(mostviewedimage1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(mostviewedimage1.getAbsolutePath());
                            ((ImageView)findViewById(R.id.driveimage5)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(driveactivity5.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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

    private void populatetrack() {
        tracks=new ArrayList<>();
        tracks.add(new Track("Swing Beats","Jazz Hop",R.drawable.party1));
        tracks.add(new Track("Funk Lofi HipHop Mix","Upbeat",R.drawable.party2));
        tracks.add(new Track("Joyride (Upbeat Lo-fi)","Sunday",R.drawable.party3));
        tracks.add(new Track("old songs but it's lofi","EMix",R.drawable.party4));
        tracks.add(new Track("Dreamworlds.","chromonicci ",R.drawable.party5));
        tracks.add(new Track("When to Say Goodbye","Ward Wills",R.drawable.party6));
        tracks.add(new Track("JazzHop","Manhattan",R.drawable.party7));
        tracks.add(new Track("late night vibes....mp3","Unknown!!!",R.drawable.party8));
        tracks.add(new Track("lofi songs slow days","slow69",R.drawable.party9));
        tracks.add(new Track("Just wanna stay here forever lofi hip hop mix","MIX",R.drawable.party10));
        tracks.add(new Track("O DOG","TYRELLSKRT prod. NOXYGEN",R.drawable.party11));
        tracks.add(new Track("Bulimia","WETT BRAiN ft.ITSOKTOCRY",R.drawable.party12));
        tracks.add(new Track("BLUPILL","DROPKICK",R.drawable.party13));
        tracks.add(new Track("Comet","Wavele$$",R.drawable.party14));
        tracks.add(new Track("Metal In Me","Kamiyada+",R.drawable.party15));
        tracks.add(new Track("Crying out","Kasper Fell prod. Yago",R.drawable.party16));
    }
    private void prepareAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(driveactivity5.this);
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
                if(null!=info){
                    if (null!=info)
                        isplaying=true;
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    // onTrackPlay();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }else {
                    startActivity(new Intent(driveactivity5.this,nointernetactivity.class));
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying())
                {
                    isplaying=false;
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
                    if(null!=info)
                    {
                        mediaPlayer.reset();
                        try {

                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            mediaPlayer.setDataSource(Drive_Song5[rand_int2]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            shuffle.setVisibility(View.INVISIBLE);
                            // CreateNotification.createNotification(driveactivity5.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive5_Audio[rand_int2]);
                            gaudiotext.setText(Drive5_artistname[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(driveactivity5.this,nointernetactivity.class));
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
                    if(null!=info)
                    {
                        mediaPlayer.reset();
                        try {

                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            shuffle.setVisibility(View.INVISIBLE);
                            mediaPlayer.setDataSource(Drive_Song5[rand_int1]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            //CreateNotification.createNotification(driveactivity5.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive5_Audio[rand_int1]);
                            gaudiotext.setText(Drive5_artistname[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(driveactivity5.this,nointernetactivity.class));
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
                handler.removeCallbacks(updater);
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
              notificationManager.cancelAll();
            }
        }
    }

    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(driveactivity5.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
        mediaPlayer.start();
        updateSeekBAr();
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.VISIBLE);
        isplaying=true;

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
            //notificationManager.cancelAll();
        unregisterReceiver(broadcastReceiver);
        isplaying=false;
    }

    @Override
    public void onTrackPause() {
        if(mediaPlayer.isPlaying())
        {
            CreateNotification.createNotification(driveactivity5.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
            isplaying=false;
        }

    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String Drive5_Audio[];
        String Drive5_Artistname[];
        int Drive5_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.Drive5_Audio=songname;
            this.Drive5_Artistname=artistname;
            this.Drive5_images=images;
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
            songname.setText(Drive5_Audio[position].toString());
            imageView.setImageResource(Drive5_images[position]);
            artistname.setText(Drive5_Artistname[position].toString());
            return listitem;

        }
    }
}