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

public class Anshumaan extends AppCompatActivity implements View.OnClickListener,Playable {
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
           // notificationManager.cancelAll();

        }
        else if (mediaPlayer.isPlaying() || mbottomsheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            mbottomsheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }
    ArrayList<String> songs=new ArrayList<String>();
    String[] Anshumaan_Audio={"iktara","channa mereya","khairiyat","Agar Tum Saath Ho","Tera Ban Jaunga","Qismat","Main Yahaan Hoon","Tum Hi Ho","Zara Thehro","Bhula Dena"};
    String[] Anshumaan_artistname={"Anshumaan","arijit singh(Remix Anshumaan)","arijit singh(Remix Anshumaan)","Alka Yagnik(Remix Anshumaan)","Jubin Nautiyal { slowed & reverbed }","Ammy Virk","Udit Narayan","arijit singh(Remix Anshumaan)","Armaan Malik, Tulsi Kumar","Anshumaan"};
    int Anshumaan_images[]={R.drawable.anshu1,R.drawable.anshu2,R.drawable.anshu3,R.drawable.anshu4,R.drawable.anshu5,R.drawable.anshu6,R.drawable.anshu7,R.drawable.anshu8,R.drawable.anshu9,R.drawable.anshu10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anshumaan);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Anshumaan_Audio.length);
        rand_int2=rand2.nextInt(Anshumaan_Audio.length)+1;
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
        InterstitialAd.load(Anshumaan.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Anshumaan_artistname[currentPosition].toString());
            gaudiotext.setText(Anshumaan_Audio[currentPosition].toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Anshumaan%2Fyt1s.com%20-%20iktara%20%20wake%20up%20sid%20slowed%20%20reverb.mp3?alt=media&token=68369b4f-3fe7-416a-92f4-519fdaec2416");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Anshumaan%2Fyt1s.com%20-%20channa%20mereya%20%20arijit%20singhslowed%20%20reverb.mp3?alt=media&token=4a705508-a223-49b5-adc8-c4cbd212da77");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Anshumaan%2Fyt1s.com%20-%20khairiyat%20%20arijit%20singh%20slowed%20%20reverbed.mp3?alt=media&token=6e26ab3f-2f38-417e-bb56-fb0b48a2e482");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Anshumaan%2Fyt1s.com%20-%20Agar%20Tum%20Saath%20Ho%20%20Alka%20Yagnik%20%20slowed%20%20reverbed%20.mp3?alt=media&token=53d4f00c-3e92-442c-a2b4-ef18c398c4ec");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Anshumaan%2Fyt1s.com%20-%20Tera%20Ban%20Jaunga%20%20Jubin%20Nautiyal%20%20slowed%20%20reverbed%20.mp3?alt=media&token=0ec19675-e37b-42fd-80be-71fdd32e50ed");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Anshumaan%2Fyt1s.com%20-%20Qismat%20%20Ammy%20Virk%20slowed%20%20reverbed.mp3?alt=media&token=db8b9330-139d-4d73-ad4f-eaafc24df293");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Anshumaan%2Fyt1s.com%20-%20Main%20Yahaan%20Hoon%20%20Udit%20Narayan%20slowed%20%20reverbed.mp3?alt=media&token=89b528bc-c2f1-4364-9ed9-74a0d0f125e0");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Anshumaan%2Fyt1s.com%20-%20Tum%20Hi%20Ho%20%20Arijit%20Singh%20slowed%20%20reverbed%20Aashiqui%202.mp3?alt=media&token=ad3b0a2e-e4bf-4021-ba01-e1f337632252");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Anshumaan%2Fyt1s.com%20-%20Zara%20Thehro%20%20Armaan%20Malik%20Tulsi%20Kumar%20slowed%20%20reverbed.mp3?alt=media&token=e5e79a91-bf2b-4f89-a173-f43b4685e102");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Anshumaan%2Fyt1s.com%20-%20Bhula%20Dena%20%20Aashiqui%202%20%20slowed%20%20reverbed.mp3?alt=media&token=31bac74d-2ccc-444b-9c7f-4081257ef870");


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
           // createChannel();
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
        storageReferenceTucker= FirebaseStorage.getInstance().getReference().child("Anshumaan/background.jpg");

        tuckerozneaudio=(ListView)findViewById(R.id.mostviewed1list);
        MyAdapter adapter=new  MyAdapter(this ,Anshumaan_Audio,Anshumaan_artistname,Anshumaan_images);
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
                            gartisttext.setText(Anshumaan_artistname[currentPosition].toString());
                            gaudiotext.setText(Anshumaan_Audio[currentPosition].toString());
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
                            gartisttext.setText(Anshumaan_artistname[currentPosition].toString());
                            gaudiotext.setText(Anshumaan_Audio[currentPosition].toString());
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
                    audioname=Anshumaan_Audio[position];
                    name=  Anshumaan_artistname[position];
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
                    gartisttext.setText(Anshumaan_artistname[position]);
                    gaudiotext.setText(Anshumaan_Audio[position]);

                }else {
                    startActivity(new Intent(Anshumaan.this,nointernetactivity.class));
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
                    Toast.makeText(Anshumaan.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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

    private void AdforoneHour(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isplaying && mInterstitialAd != null) {
                    mediaPlayer.pause();
                    mInterstitialAd.show(Anshumaan.this);
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
        tracks.add(new Track("iktara","Anshumaan",R.drawable.anshu1));
        tracks.add(new Track("channa mereya","arijit singh(Remix Anshumaan)",R.drawable.anshu2));
        tracks.add(new Track("khairiyat","arijit singh(Remix Anshumaan)",R.drawable.anshu3));
        tracks.add(new Track("Agar Tum Saath Ho","Alka Yagnik(Remix Anshumaan)",R.drawable.anshu4));
        tracks.add(new Track("Tera Ban Jaunga","Jubin Nautiyal { slowed & reverbed }",R.drawable.anshu5));
        tracks.add(new Track("Qismat","Ammy Virk",R.drawable.anshu6));
        tracks.add(new Track("Main Yahaan Hoon","Udit Narayan",R.drawable.anshu7));
        tracks.add(new Track("Tum Hi Ho","arijit singh(Remix Anshumaan)",R.drawable.anshu8));
        tracks.add(new Track("Zara Thehro","Armaan Malik, Tulsi Kumar",R.drawable.anshu9));
        tracks.add(new Track("Bhula Dena","Anshumaan",R.drawable.anshu10));
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
            mInterstitialAd.show(Anshumaan.this);
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
                    startActivity(new Intent(Anshumaan.this,nointernetactivity.class));
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
                            // CreateNotification.createNotification(Anshumaan.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Anshumaan_artistname[rand_int2]);
                            gaudiotext.setText(Anshumaan_Audio[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(Anshumaan.this,nointernetactivity.class));
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
                            // CreateNotification.createNotification(Anshumaan.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Anshumaan_artistname[rand_int1]);
                            gaudiotext.setText(Anshumaan_Audio[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(Anshumaan.this,nointernetactivity.class));
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
        CreateNotification.createNotification(Anshumaan.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
    }

    @Override
    public void onTrackPause() {
        if(mediaPlayer.isPlaying())
        {
            CreateNotification.createNotification(Anshumaan.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
            isplaying=false;
        }



    }
    class MyAdapter extends ArrayAdapter {
        Context context;
        MediaPlayer mediaPlayer;
        String Anshumaan_Audio[];
        String Anshumaan_Artistname[];
        int Anshumaan_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.Anshumaan_Audio=songname;
            this.Anshumaan_Artistname=artistname;
            this.Anshumaan_images=images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listitem=layoutInflater.inflate(R.layout.tuckerlistitem,parent,false);
            ImageView imageView=listitem.findViewById(R.id.audioimage);
            songname=listitem.findViewById(R.id.tuckeraudioname);
            artistname=listitem.findViewById(R.id.tuckeraudioartistname);
            imageView.setImageResource(Anshumaan_images[position]);
            songname.setText(Anshumaan_Audio[position].toString());
            artistname.setText(Anshumaan_Artistname[position].toString());


            return listitem;

        }
    }
}