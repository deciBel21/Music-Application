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

public class Advait extends AppCompatActivity implements View.OnClickListener,Playable {
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
    String[] Advait_Audio={"Raato Me Hai Nasha","Dil Pukare","O Meri Jaan🌃"," Maya","Teri Parchhai","Hosh walon ko","Tumhe milna","Mishri Si Hasi","Main So Gaya","Befikar"};
    String[] Advait_artistname={"Advait x Yoshita","Yoshita(Prod.Advait)","Advait","Advait x Hemang","Advait x Nuke ft. Bri Muso","Advait","Advait, Abhinav Sharma","Advait x Pahaad","Advait x P.N.D.A","Advait, Yagvik"};
    int Advait_images[]={R.drawable.advait1,R.drawable.advait2,R.drawable.advait3,R.drawable.advait4,R.drawable.advait5,R.drawable.advait6,R.drawable.advait7,R.drawable.advait8,R.drawable.advait9,R.drawable.advait10};

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
        setContentView(R.layout.activity_advait);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Advait_Audio.length);
        rand_int2=rand2.nextInt(Advait_Audio.length)+1;
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
        prepareAD();
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(Advait.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Advait_artistname[currentPosition].toString());
            gaudiotext.setText(Advait_Audio[currentPosition].toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Advait%2Fyt1s.com%20-%20Advait%20x%20Yoshita%20%20Raato%20Me%20Hai%20Ik%20Nasha.mp3?alt=media&token=d10d8268-7c45-4dad-9755-758835740517");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Advait%2Fyt1s.com%20-%20Yoshita%20%20Dil%20Pukare%20Prod%20Advait.mp3?alt=media&token=3fff4d04-53ce-4ea8-a5f5-d836e5a6e57b");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Advait%2Fyt1s.com%20-%20O%20Meri%20Jaan%20but%20sabki%20Life%20in%20a%20Metro%20kaaafi%20nostalgia%20me%20katrai%20hai%20.mp3?alt=media&token=bd347db4-6d15-4ab2-98d4-08fa9001558c");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Advait%2Fyt1s.com%20-%20Advait%20x%20Hemang%20%20Maya.mp3?alt=media&token=fc985c8b-913b-4392-8ce8-d71e0aa876f7");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Advait%2Fyt1s.com%20-%20Advait%20x%20Nuke%20%20Teri%20Parchhai%20ft%20Bri%20Muso.mp3?alt=media&token=ab5d9045-f92c-494a-bf6d-84263a409a55");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Advait%2Fyt1s.com%20-%20Hosh%20walon%20ko%20but%20papa%20dusre%20kamre%20me%20mast%20Jagjit%20Singh%20par%20vibe%20karae%20hai%20%20baarish%20ofc.mp3?alt=media&token=c481b934-f52d-4002-aee0-6ef1ef97a881");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Advait%2Fyt1s.com%20-%20Advait%20Abhinav%20Sharma%20%20Tum%20me%20milna%20Chahta%20hu%20mai.mp3?alt=media&token=8f5cbd8c-05b5-4aaf-9d25-bb340d99450a");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Advait%2Fyt1s.com%20-%20Advait%20x%20Pahaad%20%20Mishri%20Si%20Hasi.mp3?alt=media&token=3f9da9f3-a58c-4d36-b42a-b61685248185");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Advait%2Fyt1s.com%20-%20Advait%20x%20PNDA%20%20Main%20So%20Gaya%20Hoon%20Lyric%20Video.mp3?alt=media&token=9fc95b78-c02f-4fcf-988c-9654d9ae3309");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Advait%2Fyt1s.com%20-%20Advait%20Yagvik%20%20Befikar.mp3?alt=media&token=2f5e97e6-e86a-49d1-bc91-ad541efc8a7b");
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
        storageReferenceTucker= FirebaseStorage.getInstance().getReference().child("Advait/background.jpg");

        tuckerozneaudio=(ListView)findViewById(R.id.mostviewed1list);
        MyAdapter adapter=new MyAdapter(this ,Advait_Audio,Advait_artistname,Advait_images);
        tuckerozneaudio.setAdapter(adapter);
        tuckerozneaudio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition = position;
                        currentPosition = (currentPosition + 1) % songs.size();
                        Uri uri = Uri.parse(songs.get(currentPosition).toString());
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Advait_artistname[currentPosition].toString());
                            gaudiotext.setText(Advait_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition = position;
                        currentPosition = (currentPosition - 1) % songs.size();
                        Uri uri = Uri.parse(songs.get(currentPosition).toString());
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.reset();
                            previous.setVisibility(View.INVISIBLE);
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Advait_artistname[currentPosition].toString());
                            gaudiotext.setText(Advait_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }


                    }
                });
                manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (null != info) {
                    currentPosition = position;
                    audioname = Advait_Audio[position];
                    name = Advait_artistname[position];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(position));
                            mediaPlayer.prepare();

                        } else {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(position));
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //if (isplaying){
                    //  onTrackPause();
                    //}
                    //else{
                    // onTrackPlay();
                    //}
                    registerReceiver(noisyAudioStreamreceiver, intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    gartisttext.setText(Advait_artistname[position]);
                    gaudiotext.setText(Advait_Audio[position]);

                } else {
                    startActivity(new Intent(Advait.this, nointernetactivity.class));
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
                    Toast.makeText(Advait.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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

   // private void createChannel() {
       // if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
          //  NotificationChannel channel=new NotificationChannel(CreateNotification.CHANNEL_ID,
           //         "Decibel Inc", NotificationManager.IMPORTANCE_LOW);
           // notificationManager=getSystemService(NotificationManager.class);
            //if (notificationManager !=null){
               // notificationManager.createNotificationChannel(channel);
           // }
       // }
   // }

    private void populatetrack() {
        tracks=new ArrayList<>();
        tracks.add(new Track("Raato Me Hai Nasha","Advait x Yoshita",R.drawable.advait1));
        tracks.add(new Track("Dil Pukare","Yoshita(Prod.Advait)",R.drawable.advait2));
        tracks.add(new Track("O Meri Jaan🌃","Advait",R.drawable.advait3));
        tracks.add(new Track("Maya","Advait x Hemang",R.drawable.advait4));
        tracks.add(new Track("Teri Parchhai","Advait x Nuke ft. Bri Muso",R.drawable.advait5));
        tracks.add(new Track("Hosh walon ko","Advait",R.drawable.advait6));
        tracks.add(new Track("Tumhe milna","Advait, Abhinav Sharma",R.drawable.advait7));
        tracks.add(new Track("Mishri Si Hasi","Advait x Pahaad",R.drawable.advait8));
        tracks.add(new Track("Main So Gaya","Advait x P.N.D.A",R.drawable.advait9));
        tracks.add(new Track("Befikar","Advait, Yagvik",R.drawable.advait10));
    }
    private Runnable updater= new Runnable() {
        @Override
        public void run() {
            updateSeekBAr();
            long currentduration=mediaPlayer.getCurrentPosition();
        }
    };
    private void AdforoneHour(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isplaying && mInterstitialAd != null) {
                    mediaPlayer.pause();
                    mInterstitialAd.show(Advait.this);
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
    private void prepareAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(Advait.this);
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
                manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (null != info)
                {
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    // onTrackPlay();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }else {
                    startActivity(new Intent(Advait.this,nointernetactivity.class));
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
                    manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    info = manager.getActiveNetworkInfo();
                    if (null != info)
                    {
                        mediaPlayer.reset();
                        try {

                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            mediaPlayer.setDataSource(songs.get(rand_int2));
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            shuffle.setVisibility(View.INVISIBLE);
                            // CreateNotification.createNotification(Advait.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Advait_artistname[rand_int2]);
                            gaudiotext.setText(Advait_Audio[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(Advait.this,nointernetactivity.class));
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
                    manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    info = manager.getActiveNetworkInfo();
                    if (null != info)
                    {
                        mediaPlayer.reset();
                        try {
                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            shuffle.setVisibility(View.INVISIBLE);
                            mediaPlayer.setDataSource(songs.get(rand_int1));
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            // CreateNotification.createNotification(Advait.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Advait_artistname[rand_int1]);
                            gaudiotext.setText(Advait_Audio[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(Advait.this,nointernetactivity.class));
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
        CreateNotification.createNotification(Advait.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(Advait.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
        String Advait_Audio[];
        String Advait_Artistname[];
        int Advait_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.Advait_Audio=songname;
            this.Advait_Artistname=artistname;
            this.Advait_images=images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listitem=layoutInflater.inflate(R.layout.tuckerlistitem,parent,false);
            ImageView imageView=listitem.findViewById(R.id.audioimage);
            songname=listitem.findViewById(R.id.tuckeraudioname);
            artistname=listitem.findViewById(R.id.tuckeraudioartistname);
            imageView.setImageResource(Advait_images[position]);
            songname.setText(Advait_Audio[position].toString());
            artistname.setText(Advait_artistname[position].toString());


            return listitem;

        }
    }
}