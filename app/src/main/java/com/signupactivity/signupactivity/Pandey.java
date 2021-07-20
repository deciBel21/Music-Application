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

public class Pandey extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView tuckermainimage;
    private static final String TAG = "TuckerZone";
    private InterstitialAd mInterstitialAd;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    MediaPlayer mediaPlayer;
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
    ConnectivityManager manager;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying())
        {
            AdforoneHour();
            Toast.makeText(this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            bottomshettplayer.setVisibility(View.INVISIBLE);
            //notificationManager.cancelAll();

        }
        else if (mediaPlayer.isPlaying() || mbottomsheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            mbottomsheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }
    ArrayList<String> songs=new ArrayList<String>();
    String[] Pandey_Audio={"Tounge Tied","Crop Top","Sunrise","Rozay","Remind Me Of You","Tyler Herro","You And I","On My Lowkey","Campfire Freestyle","Money Right"};
    String[] Pandey_artistname={"skylarallen","Lil No Name","YJKL","Rocky","Juice WRLD & The Kid Laroi","Jack Harlow","Purpprxmi","Karma x","Juice WRLD","Karma x"};
    int Pandey_images[]={R.drawable.yuko1,R.drawable.yuko2,R.drawable.yuko3,R.drawable.yuko4,R.drawable.yuko4,R.drawable.yuko6,R.drawable.yuko7,R.drawable.yuko8,R.drawable.yuko9,R.drawable.yuko10};

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandey);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Pandey_Audio.length);
        rand_int2=rand2.nextInt(Pandey_Audio.length)+1;
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
        InterstitialAd.load(Pandey.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Pandey_artistname[currentPosition].toString());
            gaudiotext.setText(Pandey_Audio[currentPosition].toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Pandey%2Fyt1s.com%20-%20skylarallen%20%20TONGUE%20TIED%20PROD%20GMRY.mp3?alt=media&token=b9838dbe-c985-4c5e-a552-3708311e8707");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Pandey%2Fyt1s.com%20-%20Lil%20No%20Name%20%20Crop%20Top.mp3?alt=media&token=a0791ac4-d0a9-406d-9e8e-0102982b2637");
         songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Pandey%2Fyt1s.com%20-%20YJKL%20%20Sunrise.mp3?alt=media&token=613e9997-d87d-4436-ba5c-c53ba87712f4");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Pandey%2Fyt1s.com%20-%20Rocky%20RoZay%20%20Rozay.mp3?alt=media&token=5d452610-f90c-4cb3-945a-96bfae6fc537");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Pandey%2Fyt1s.com%20-%20Juice%20WRLD%20%20The%20Kid%20Laroi%20%20Remind%20Me%20Of%20You%20Official%20Lofi%20Remix%20.mp3?alt=media&token=b86080cc-5663-46a5-8de5-5d7c193a597a");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Pandey%2Fyt1s.com%20-%20Jack%20Harlow%20%20Tyler%20Herro%20Official%20Lofi%20Remix.mp3?alt=media&token=d5ea2f4d-3d6b-4621-9e05-f29bbcddacda");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Pandey%2Fyt1s.com%20-%20Purpprxmi%20%20You%20And%20I%20prod%20Ross%20Gossage%20x%20Nick%20Mira.mp3?alt=media&token=813b24d8-a3c7-4b08-bd32-bb2939608c91");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Pandey%2Fyt1s.com%20-%20Karma%20x%20%20On%20My%20Lowkey.mp3?alt=media&token=22cbb121-0e8f-4cba-b908-72bc61f1be00");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Pandey%2Fyt1s.com%20-%20Juice%20WRLD%20%20Campfire%20Freestyle%20Official%20Lofi%20Remix.mp3?alt=media&token=85e17ff0-5e0c-4f0d-9bb5-271f6f96e82f");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Pandey%2Fyt1s.com%20-%20Karma%20x%20%20Money%20Right.mp3?alt=media&token=bbc8e382-6096-4dcf-be0c-ac9c7b27e355");

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
        storageReferenceTucker= FirebaseStorage.getInstance().getReference().child("Pandey/displaypic.jpg");

        tuckerozneaudio=(ListView)findViewById(R.id.mostviewed1list);
      MyAdapter adapter=new MyAdapter(this ,Pandey_Audio,Pandey_artistname,Pandey_images);
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
                            gartisttext.setText(Pandey_artistname[currentPosition].toString());
                            gaudiotext.setText(Pandey_Audio[currentPosition].toString());
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
                            gartisttext.setText(Pandey_artistname[currentPosition].toString());
                            gaudiotext.setText(Pandey_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }


                    }
                });
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info=manager.getActiveNetworkInfo();
                if (null!=info){
                    currentPosition=position;
                    audioname=Pandey_Audio[position];
                    name=  Pandey_artistname[position];
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

              /*  if (isplaying){
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
                    gartisttext.setText(Pandey_artistname[position]);
                    gaudiotext.setText(Pandey_Audio[position]);


                }else {
                    startActivity(new Intent(Pandey.this,nointernetactivity.class));
                }

            }

        });




        final File tuckermainimage;
        try {
            tuckermainimage=File.createTempFile("mainimage","jpg");
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
                    Toast.makeText(Pandey.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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
    private Runnable updater= new Runnable() {
        @Override
        public void run() {
            updateSeekBAr();
            long currentduration=mediaPlayer.getCurrentPosition();
        }
    };
    private void prepareAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(Pandey.this);
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
    private void AdforoneHour(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isplaying && mInterstitialAd != null) {
                    mediaPlayer.pause();
                    mInterstitialAd.show(Pandey.this);
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

    private void populatetrack() {
        tracks=new ArrayList<>();
        tracks.add(new Track("Tounge Tied","skylarallen",R.drawable.yuko1));
        tracks.add(new Track("Crop Top","Lil No Name",R.drawable.yuko2));
        tracks.add(new Track("Sunrise","YJKL",R.drawable.yuko3));
        tracks.add(new Track("Rozay","Rocky",R.drawable.yuko4));
        tracks.add(new Track("Remind Me Of You","Juice WRLD & The Kid Laroi",R.drawable.yuko5));
        tracks.add(new Track("Jack Harlow","Tyler Herro",R.drawable.yuko6));
        tracks.add(new Track("You And I","Purpprxmi",R.drawable.yuko7));
        tracks.add(new Track("On My Lowkey","Karma x",R.drawable.yuko8));
        tracks.add(new Track("Campfire Freestyle","Juice WRLD",R.drawable.yuko9));
        tracks.add(new Track("Money Right","Karma x ",R.drawable.yuko10));
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
                    // onTrackPlay();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }else {
                    startActivity(new Intent(Pandey.this,nointernetactivity.class));
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
                            mediaPlayer.setDataSource(songs.get(rand_int2));
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            shuffle.setVisibility(View.INVISIBLE);
                            // CreateNotification.createNotification(Pandey.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Pandey_artistname[rand_int2]);
                            gaudiotext.setText(Pandey_Audio[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(Pandey.this,nointernetactivity.class));
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
                            mediaPlayer.setDataSource(songs.get(rand_int1));
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            //CreateNotification.createNotification(Pandey.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Pandey_artistname[rand_int1]);
                            gaudiotext.setText(Pandey_Audio[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(Pandey.this,nointernetactivity.class));
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
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
                notificationManager.cancelAll();
            }
        }
    }



    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(Pandey.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(Pandey.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
        String Pandey_Audio[];
        String Pandey_Artistname[];
        int Pandey_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.Pandey_Audio=songname;
            this.Pandey_Artistname=artistname;
            this.Pandey_images=images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listitem=layoutInflater.inflate(R.layout.tuckerlistitem,parent,false);
            ImageView imageView=listitem.findViewById(R.id.audioimage);
            songname=listitem.findViewById(R.id.tuckeraudioname);
            artistname=listitem.findViewById(R.id.tuckeraudioartistname);
            imageView.setImageResource(Pandey_images[position]);
            songname.setText(Pandey_Audio[position].toString());
            artistname.setText(Pandey_Artistname[position].toString());


            return listitem;

        }
    }
}