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

public class mostviewed3 extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView mostviewimage3;
    List<Track> tracks;
    NotificationManager notificationManager;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    int currentPosition;
    private BottomSheetBehavior mbottomsheetBehavior;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "viewed3";
    SeekBar Scrubber;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    public boolean isplaying=false;
    SeekBar VolumeControl;
    private Handler handler=new Handler();

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
        if (mediaPlayer.isPlaying() )
        {
            prepareAD();
            Toast.makeText(this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            //notificationManager.cancelAll();
            bottomshettplayer.setVisibility(View.INVISIBLE);
        }
    }

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
    int rand_int1;
    int rand_int2;

    Button shuffle;
    private StorageReference storageReferenceMostviewed3;
    TextView songname;
    TextView artistname;
    String[] Viewed_Song3={"https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FCrowander%20-%20Dancing%20on%20the%20Sidewalk.mp3?alt=media&token=93a5797d-cd3b-4d83-a0e8-0805600d5253","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FCrowander%20-%20Going%20Underground.mp3?alt=media&token=def1057d-497f-4e3c-a0e2-202bd136764d","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FCrowander%20-%20Have%20a%20Smoke.mp3?alt=media&token=9feb2116-7431-4eca-a9d7-4a7fc8a79aa3","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FCrowander%20-%20Last%20Drink.mp3?alt=media&token=4ab9884d-81a9-4c94-b699-7d629973e5f9","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FCrowander%20-%20Neon%20Lights.mp3?alt=media&token=004e6a25-b7ab-4780-8b20-27ecc920f29b","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FCrowander%20-%20School%20Yard.mp3?alt=media&token=49362654-e372-492c-881b-c9a8d17a6895","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FCrowander%20-%20Stop%20on%20a%20Bench.mp3?alt=media&token=a50e5857-230e-4fd9-9a30-d50c1ab13ff6","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album3%2Fyt1s.com%20-%20Imfinenow%20%20S%C3%B8lace%20%20Like%20You%20Were%20With%20Me%20ft%20Madson.mp3?alt=media&token=aafeaec8-d454-452e-8e98-97e1d2b38461","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album3%2Fyt1s.com%20-%20Monty%20Datta%20%20Hinshi%20%20Anxiety%20Arise.mp3?alt=media&token=bed580e0-4e7e-4076-9090-b2202cf8672b","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album3%2Fyt1s.com%20-%20swablu%20%20Artemis%20Orion%20%20phantom%20feelings.mp3?alt=media&token=74cbd5b8-278b-4b5e-b9c0-f2793ded8692","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album3%2Fyt1s.com%20-%20Oui%20Lele%20%20Julia%20Alexa%20%20sry%20i%20like%20u.mp3?alt=media&token=61975067-accc-4092-83d4-04a4a44c03b9","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album3%2Fyt1s.com%20-%20ondi%20vil%20%20i%20wish%20that%20she%20never%20did%20ft%20mishaal.mp3?alt=media&token=17726351-dfa5-4aba-a43d-cd7855977656","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album3%2Fyt1s.com%20-%20Rnla%20%20Feels%20Like%20The%20World%20Is%20Ending%20ft%20yaeow.mp3?alt=media&token=ece227a2-a793-4328-81ab-ab6acfa7343a","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album3%2Fyt1s.com%20-%20Kayou%20%20Woven%20In%20Hiatus%20%20these%20days%20will%20fade%20out.mp3?alt=media&token=d6d07465-8460-4351-8d45-4883049489ff","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album3%2Fyt1s.com%20-%20N%C3%9C%20%20Dylsm%20%20i%20tried%20to%20get%20to%20you%20ft%20yaeow.mp3?alt=media&token=83a17ff1-7dcc-4563-b641-4105b8a85749","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album3%2Fyt1s.com%20-%20Hypx%20%20Lost%20Soul%20ft%20S%C3%B8lace.mp3?alt=media&token=23f744c6-3e53-4e2a-b61c-7107fe1f9431"};
    String[] Mostviewed3_Audio = {" Dancing on the Sidewalk", "Going Underground", "Have a Smoke", "Last Drink", "Neon Lights", "School Yard", "Stop on a Bench","Like You Were","Anxiety Arise","phantom feelings","sry, i like u","i wish that she never","The World Is Ending","these days will fade","tried to get you","Lost Soul"};
    String[] Mostviewed3_artistname = {"Crowander"};
    int Mostviewed3_images[] = {R.drawable.mb1,R.drawable.mb2,R.drawable.mb3,R.drawable.mb4,R.drawable.mb5,R.drawable.mb6,R.drawable.mb7,R.drawable.mb8,R.drawable.mb9,R.drawable.mb10,R.drawable.mb11,R.drawable.mb12,R.drawable.mb13,R.drawable.mb14,R.drawable.mb15,R.drawable.mb16,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostviewed3);
        shuffle = (Button) findViewById(R.id.shuffle);
        shuffle.setOnClickListener(this);
        Random rand = new Random();
        Random rand2 = new Random();
        previous=(ImageView)findViewById(R.id.previous);
        next=(ImageView)findViewById(R.id.next);
        rand_int1=rand.nextInt(Mostviewed3_Audio.length);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        rand_int2=rand2.nextInt(Mostviewed3_artistname.length)+1;
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
        InterstitialAd.load(mostviewed3.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Mostviewed3_artistname[0].toString());
            gaudiotext.setText(Mostviewed3_Audio[currentPosition].toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bottomshettplayer.setVisibility(View.INVISIBLE);
               // notificationManager.cancelAll();
                //prepareAD();

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


        mostviewimage3 = (ImageView) findViewById(R.id.mostviewimage3);
        storageReferenceMostviewed3 = FirebaseStorage.getInstance().getReference().child("Playlist/mostv3.jpg");
       MyAdapter adapter=new MyAdapter(this,Mostviewed3_Audio,Mostviewed3_artistname,Mostviewed3_images);
        ListView mostviewedaudiolist3 = (ListView) findViewById(R.id.mostviewedlistog3);
        mostviewedaudiolist3.setAdapter(adapter);
        mostviewedaudiolist3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Viewed_Song3.length;
                        Uri uri=Uri.parse(Viewed_Song3[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed3_artistname[currentPosition].toString());
                            gaudiotext.setText(Mostviewed3_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Viewed_Song3.length;
                        Uri uri=Uri.parse(Viewed_Song3[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed3_artistname[currentPosition].toString());
                            gaudiotext.setText(Mostviewed3_Audio[currentPosition].toString());
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
                    audioname=Mostviewed3_Audio[position];
                    name=  Mostviewed3_artistname[0];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Viewed_Song3[position]);
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Viewed_Song3[position]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    startActivity(new Intent(mostviewed3.this,nointernetactivity.class));
                }

               /* if (isplaying){
                    onTrackPause();
                }
                else{
                    onTrackPlay();
                }*/
                registerReceiver(noisyAudioStreamreceiver,intentFilter);
                updateSeekBAr();
                mediaPlayer.start();
                handler.removeCallbacks(updater);
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.INVISIBLE);
                gartisttext.setText(Mostviewed3_artistname[0]);
                gaudiotext.setText(Mostviewed3_Audio[position]);

            }

        });


        final File mostviewedimage1;
        try {
            mostviewedimage1 = File.createTempFile("mostv3", "jpg");
            storageReferenceMostviewed3.getFile(mostviewedimage1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(mostviewedimage1.getAbsolutePath());
                            ((ImageView) findViewById(R.id.mostviewimage3)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mostviewed3.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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

    private void populatetrack() {
        tracks=new ArrayList<>();
        tracks.add(new Track("Dancing on the Sidewalk","Crowander",R.drawable.mb1));
        tracks.add(new Track("Going Underground","Crowander",R.drawable.mb2));
        tracks.add(new Track("Have a Smoke","Crowander",R.drawable.mb3));
        tracks.add(new Track("Last Drink","Crowander",R.drawable.mb4));
        tracks.add(new Track("Neon Lights","Crowander",R.drawable.mb5));
        tracks.add(new Track("School Yard","Crowander",R.drawable.mb6));
        tracks.add(new Track("Stop on a Bench","Crowander",R.drawable.mb7));
        tracks.add(new Track("Like You Were","Crowander",R.drawable.mb8));
        tracks.add(new Track("Anxiety Arise","Crowander",R.drawable.mb9));
        tracks.add(new Track("phantom feelings","Crowander",R.drawable.mb10));
        tracks.add(new Track("sry, i like u","Crowander",R.drawable.mb11));
        tracks.add(new Track("i wish that she never","Crowander",R.drawable.mb12));
        tracks.add(new Track("The World Is Ending","Crowander",R.drawable.mb13));
        tracks.add(new Track("these days will fade","Crowander",R.drawable.mb14));
        tracks.add(new Track("tried to get you","Crowander",R.drawable.mb15));
        tracks.add(new Track("Lost Soul","Crowander",R.drawable.mb16));
    }
    private void prepareAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(mostviewed3.this);
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
                    startActivity(new Intent(mostviewed3.this,nointernetactivity.class));
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying())
                {
                    //prepareAD();
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
                            mediaPlayer.setDataSource(Viewed_Song3[rand_int2]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            shuffle.setVisibility(View.INVISIBLE);
                            //CreateNotification.createNotification(mostviewed3.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed3_artistname[0]);
                            gaudiotext.setText(Mostviewed3_Audio[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(mostviewed3.this,nointernetactivity.class));
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
                            mediaPlayer.setDataSource(Viewed_Song3[rand_int1]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            //CreateNotification.createNotification(mostviewed3.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed3_artistname[0]);
                            gaudiotext.setText(Mostviewed3_Audio[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(mostviewed3.this,nointernetactivity.class));
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
        CreateNotification.createNotification(mostviewed3.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(mostviewed3.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
           // notificationManager.cancelAll();
        unregisterReceiver(broadcastReceiver);
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String Mostviewed3_Audio[];
        String Mostviewed3_Artistname[];
        int Mostviewed3_images[];


        public MyAdapter(@NonNull Context c, String songname[], String artistname[], int images[]) {
            super(c, R.layout.tuckerlistitem, R.id.tuckeraudioname, songname);
            this.context = c;
            this.Mostviewed3_Audio = songname;
            this.Mostviewed3_Artistname = artistname;
            this.Mostviewed3_images = images;
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
            songname.setText(Mostviewed3_Audio[position].toString());
            imageView.setImageResource(Mostviewed3_images[position]);
            artistname.setText(Mostviewed3_Artistname[0].toString());
            return listitem;
        }
    }
    }
