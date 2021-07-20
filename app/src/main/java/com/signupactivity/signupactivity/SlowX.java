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

public class SlowX extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView tuckermainimage;
    private static final String TAG = "TuckerZone";
    private InterstitialAd mInterstitialAd;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    MediaPlayer mediaPlayer;
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

    TextView title;
    List<Track> tracks;
    AudioManager audioManager;
    NotificationManager notificationManager;
    int currentPosition;
    SeekBar Scrubber;
    SeekBar VolumeControl;
    private Handler handler=new Handler();
    int MaxVolume;
    int CurVolume;
    ImageView mostviewimage3;
    private BottomSheetBehavior mbottomsheetBehavior;
    ImageView play;
    ImageView play1;
    ImageView pause;
    ImageView pause1;
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

    Button shuffle;
    private StorageReference storageReferenceTucker;
    ListView tuckerozneaudio;
    TextView songname;
    TextView artistname;
    int rand_int1;
    int rand_int2;
    public boolean isplaying=false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying())
        {
            AdforoneHour();
            Toast.makeText(this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            bottomshettplayer.setVisibility(View.INVISIBLE);
            notificationManager.cancelAll();

        }
        else if (mediaPlayer.isPlaying() || mbottomsheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            mbottomsheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }
    ArrayList<String> songs=new ArrayList<String>();
    String[] SlowX_Audio={"I Need U"," melancholy","night changes","Jimmy","party favor","i don't trust nobody","mood","At My Worst","slow dancing","walking through"};
    String[] SlowX_artistname={"yaeow","Gustixa & White Cherry","Gustixa ft. Alsa","Rxseboy, Sarcastic Sounds","Gustixa Remix","Gustixa","Gustixa Remix","Pink Sweat$","Gustixa Remix","Gustixa ft. Mishaal"};
    int SlowX_images[]={R.drawable.gustixa1,R.drawable.gustixa2,R.drawable.gustixa3,R.drawable.gustixa4,R.drawable.gustixa5,R.drawable.gustixa6,R.drawable.gustixa7,R.drawable.gustixa8,R.drawable.gustixa9,R.drawable.gustixa10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slow_x);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(SlowX_Audio.length);
        rand_int2=rand2.nextInt(SlowX_Audio.length)+1;
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
        intentFilter=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        noisyAudioStreamreceiver=new NoisyAudioStreamreceiver();
        next=(ImageView)findViewById(R.id.next);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(SlowX.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
                }else if (focusChange==AudioManager.AUDIOFOCUS_GAIN){
                    if (playingBeforeInterruption){
                        mediaPlayer.start();
                    }
                }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS){
                    mediaPlayer.pause();
                    audioManager.abandonAudioFocus(afChangeListener);
                }
            }
        };
        if (mediaPlayer.isPlaying())
        {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.INVISIBLE);
            gartisttext.setText(SlowX_artistname[currentPosition].toString());
            gaudiotext.setText(SlowX_Audio[currentPosition].toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/SlowXReverb%2Fyt1s.com%20-%20yaeow%20%20I%20Need%20U%20Gustixa%20Remix%20Official%20Audio.mp3?alt=media&token=3eb9d783-39c0-4e06-bf48-bffbdb1f3d47");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/SlowXReverb%2Fyt1s.com%20-%20Gustixa%20%20White%20Cherry%20%20melancholy%20alt%20version.mp3?alt=media&token=a2fdbbf8-c6d9-44ca-9d0f-1d72ebc38552");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/SlowXReverb%2Fyt1s.com%20-%20night%20changes%20Gustixa%20ft%20Alsa.mp3?alt=media&token=fb6a0eac-50fd-47ab-8dc1-7e31eee15116");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/SlowXReverb%2Fyt1s.com%20-%20Rxseboy%20Sarcastic%20Sounds%20%20Jimmy.mp3?alt=media&token=ff5d42f6-948e-4c97-9164-6f57b7b09084");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/SlowXReverb%2Fyt1s.com%20-%20party%20favor%20Gustixa%20Remix.mp3?alt=media&token=d348f526-6dc5-4f5c-9927-e345b61d3212");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/SlowXReverb%2Fyt1s.com%20-%20Gustixa%20%20i%20dont%20trust%20nobody.mp3?alt=media&token=87b83468-91e0-4ae1-b29a-7a0bfb6cfb0e");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/SlowXReverb%2Fyt1s.com%20-%20mood%20Gustixa%20Remix.mp3?alt=media&token=539f53b3-a508-4225-bc52-a28f270e92db");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/SlowXReverb%2Fyt1s.com%20-%20Pink%20Sweat%20%20At%20My%20Worst%20Gustixa%20Remix%20Official%20Audio.mp3?alt=media&token=ab1dde60-e69d-4ba8-ae49-cec04b2bd23a");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/SlowXReverb%2Fyt1s.com%20-%20slow%20dancing%20in%20the%20dark%20Gustixa%20Remix.mp3?alt=media&token=20859c8e-112d-44d3-a744-922f2ece3bd9");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/SlowXReverb%2Fyt1s.com%20-%20Gustixa%20%20walking%20through%20a%20memory%20ft%20Mishaal.mp3?alt=media&token=4e7e72e4-69d2-45e0-ac2e-4dc20e6fe6a7");

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



        tuckermainimage= (ImageView) findViewById(R.id.tuckermainimage);
        storageReferenceTucker= FirebaseStorage.getInstance().getReference().child("SlowXReverb/background.jpg");

        tuckerozneaudio=(ListView)findViewById(R.id.mostviewed1list);
        MyAdapter adapter=new MyAdapter(this ,SlowX_Audio,SlowX_artistname,SlowX_images);
        tuckerozneaudio.setAdapter(adapter);
        tuckerozneaudio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                            gartisttext.setText(SlowX_artistname[currentPosition].toString());
                            gaudiotext.setText(SlowX_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition-1)%songs.size();
                        Uri uri=Uri.parse(songs.get(currentPosition).toString());
                        if (mediaPlayer.isPlaying())
                        {
                            mediaPlayer.reset();
                            mediaPlayer.reset();
                            previous.setVisibility(View.INVISIBLE);
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(SlowX_artistname[currentPosition].toString());
                            gaudiotext.setText(SlowX_Audio[currentPosition].toString());
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
                    audioname=SlowX_Audio[position];
                    name=  SlowX_artistname[position];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(position));
                            mediaPlayer.prepare();

                        }
                        else
                        {   mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(position));
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
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    gartisttext.setText(SlowX_artistname[position]);
                    gaudiotext.setText(SlowX_Audio[position]);

                }else {
                    startActivity(new Intent(SlowX.this,nointernetactivity.class));
                }

            }

        });




        final File tuckermainimage;
        try {
            tuckermainimage=File.createTempFile("background","jpg");
            storageReferenceTucker.getFile(tuckermainimage)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(tuckermainimage.getAbsolutePath());
                            ((ImageView)findViewById(R.id.tuckermainimage)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SlowX.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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

    private void populatetrack() {
        tracks=new ArrayList<>();
        tracks.add(new Track("I Need U","yaeow",R.drawable.gustixa1));
        tracks.add(new Track("melancholy","Gustixa & White Cherry",R.drawable.gustixa2));
        tracks.add(new Track("night changes","Gustixa ft. Alsa",R.drawable.gustixa3));
        tracks.add(new Track("Jimmy","Rxseboy Sarcastic Sounds",R.drawable.gustixa4));
        tracks.add(new Track("party favor","Gustixa Remix",R.drawable.gustixa5));
        tracks.add(new Track("i don't trust nobody","Gustixa",R.drawable.gustixa6));
        tracks.add(new Track("mood","Gustixa Remix",R.drawable.gustixa7));
        tracks.add(new Track("At My Worst","Pink Sweat$",R.drawable.gustixa8));
        tracks.add(new Track("slow dancing","Gustixa Remix",R.drawable.gustixa9));
        tracks.add(new Track("walking through","Gustixa ft. Mishaal",R.drawable.gustixa10));
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
            mInterstitialAd.show(SlowX.this);
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
                    mInterstitialAd.show(SlowX.this);
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
                }
                else {
                    startActivity(new Intent(SlowX.this,nointernetactivity.class));
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
                    if (null!=info){
                        mediaPlayer.reset();
                        try {
                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            mediaPlayer.setDataSource(songs.get(rand_int2));
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            shuffle.setVisibility(View.INVISIBLE);
                            // CreateNotification.createNotification(SlowX.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            updateSeekBAr();
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(SlowX_artistname[rand_int2]);
                            gaudiotext.setText(SlowX_Audio[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(SlowX.this,nointernetactivity.class));
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
                        if (null!=info){
                            mediaPlayer.reset();
                            try {

                                registerReceiver(noisyAudioStreamreceiver,intentFilter);
                                shuffle.setVisibility(View.INVISIBLE);
                                mediaPlayer.setDataSource(songs.get(rand_int1));
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                updateSeekBAr();
                                CreateNotification.createNotification(SlowX.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                                pause.setVisibility(View.VISIBLE);
                                play.setVisibility(View.INVISIBLE);
                                gartisttext.setText(SlowX_artistname[rand_int1]);
                                gaudiotext.setText(SlowX_Audio[rand_int1]);
                                bottomshettplayer.setVisibility(View.VISIBLE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                            startActivity(new Intent(SlowX.this,nointernetactivity.class));
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
        CreateNotification.createNotification(SlowX.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(SlowX.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
    class MyAdapter extends ArrayAdapter {
        Context context;
        MediaPlayer mediaPlayer;
        String SlowX_Audio[];
        String SlowX_Artistname[];
        int SlowX_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.SlowX_Audio=songname;
            this.SlowX_Artistname=artistname;
            this.SlowX_images=images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listitem=layoutInflater.inflate(R.layout.tuckerlistitem,parent,false);
            ImageView imageView=listitem.findViewById(R.id.audioimage);
            songname=listitem.findViewById(R.id.tuckeraudioname);
            artistname=listitem.findViewById(R.id.tuckeraudioartistname);
            imageView.setImageResource(SlowX_images[position]);
            songname.setText(SlowX_Audio[position].toString());
            artistname.setText(SlowX_Artistname[position].toString());


            return listitem;

        }
    }
}