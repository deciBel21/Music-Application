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

public class mostviewed2 extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView mostviewimage2;
    int currentPosition;
    List<Track> tracks;
    NotificationManager notificationManager;
    private BottomSheetBehavior mbottomsheetBehavior;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "viewed2";
    SeekBar Scrubber;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    SeekBar VolumeControl;
    private Handler handler=new Handler();
    int MaxVolume;
    int CurVolume;
    ImageView previous;
    ImageView next;
    ImageView play;
    ImageView pause;
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
    private StorageReference storageReferenceMostviewed2;
    TextView songname;
    TextView artistname;
    String[] Viewed_Song2={"https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FAudiobinger%20-%20I%20Used%20to%20Love%20Hip-Hop.mp3?alt=media&token=49d7c574-c19f-42cb-8c86-afcab68d79ab","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FAudiobinger%20-%20Isolation.mp3?alt=media&token=083b4d1e-e822-496c-9a19-1ebe0363d52c","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FAudiobinger%20-%20Lonely%20Winter.mp3?alt=media&token=754f03fa-3d86-40e8-85c0-21c971c215fb","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FAudiobinger%20-%20The%20Block.mp3?alt=media&token=c1ccc20a-3caa-4ef2-8101-dede666c5148","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2FAudiobinger%20-%20The%20Upside%20Down.mp3?alt=media&token=45724229-2c56-4f79-9cb7-db502f1365b7","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20rage%20%20no%20more%20missing%20%20brazilian%20lofi%20vibes.mp3?alt=media&token=db76513e-33ad-4398-a7e9-9234e1de2936","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20ZALO%20%20Introspection.mp3?alt=media&token=f048a00b-14c5-450c-ac38-107c3d5bfd5c","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20rage%20%20kumori%20%20melancholic%20lofi.mp3?alt=media&token=1a3652fe-4fec-4d88-bc40-8430638d2180","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20Never%20%20throwback%20to%20sunshine.mp3?alt=media&token=6ab2445e-68f0-47de-8781-341d042d34ba","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20rage%20%20Homeward%20%20chill%20jazzy%20beat.mp3?alt=media&token=bde6e1d1-9cc9-4a85-9547-87945d8d0387","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20Bamf%20%20Calmness%20After%20Storm%20%20ambient%20lofi%20UNRELEASED.mp3?alt=media&token=ed0f561e-c187-4f9f-b048-5759a42c74a0","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20KASPA%20%20Searching.mp3?alt=media&token=8c7421c0-28f1-4647-8952-5f0747394b3f","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20Citia%20%20Bah%C3%ADa%20Blanca%20%20lofi%20beat.mp3?alt=media&token=e154455c-f02b-4924-91da-7d48b8ed96e7","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20Grenn%20%20Am%20I%20Floating%20%20sad%20lofi%20beat.mp3?alt=media&token=637dc1a9-5592-49fb-a4ab-39d41a42ff79","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20zendo%20%20afternoon%20tea.mp3?alt=media&token=d7cfc0d1-0fc8-4fbb-984b-7ac8ca5cf314","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Album2%2Fyt1s.com%20-%20T%20s%20u%20n%20a%20m%20i%20i%20%20peace.mp3?alt=media&token=f96bed3f-685f-4a81-a9df-bc56224cda03"};
    String[] Mostviewed2_Audio = {" Used to Love Hip-Hop", "Isolation", "Lonely Winter", "The Block", "The Upside Down","no more missing","Introspection","kumori","throwback to sunshine","Homeward","Calmness","Searching","Bahía Blanca","Am I Floating","afternoon tea","T s u n a m i"};
    String[] Mostviewed2_artistname = {"Audiobinger"};
    int Mostviewed2_images[] = {R.drawable.ma1,R.drawable.ma2,R.drawable.ma3,R.drawable.ma4,R.drawable.ma5,R.drawable.ma6,R.drawable.ma7,R.drawable.ma8,R.drawable.ma9,R.drawable.ma10,R.drawable.ma11,R.drawable.ma12,R.drawable.ma13,R.drawable.ma14,R.drawable.ma14,R.drawable.ma15,R.drawable.ma16,};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostviewed2);
        shuffle = (Button) findViewById(R.id.shuffle);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Mostviewed2_artistname.length);
        rand_int2=rand2.nextInt(Mostviewed2_Audio.length)+1;
        shuffle.setOnClickListener(this);
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
        intentFilter=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        noisyAudioStreamreceiver=new NoisyAudioStreamreceiver();
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
                        onTrackPlay();
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
        InterstitialAd.load(mostviewed2.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Mostviewed2_artistname[0].toString());
            gaudiotext.setText(Mostviewed2_Audio[currentPosition].toString());
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



        mostviewimage2 = (ImageView) findViewById(R.id.mostviewimage2);
        storageReferenceMostviewed2 = FirebaseStorage.getInstance().getReference().child("Playlist/mostv2.jpg");
       MyAdapter adapter=new MyAdapter(this,Mostviewed2_Audio,Mostviewed2_artistname,Mostviewed2_images);
        ListView mostviewedaudiolist2 = (ListView) findViewById(R.id.mostviewedlistog2);
        mostviewedaudiolist2.setAdapter(adapter);
        mostviewedaudiolist2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Viewed_Song2.length;
                        Uri uri=Uri.parse(Viewed_Song2[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed2_artistname[currentPosition].toString());
                            gaudiotext.setText(Mostviewed2_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Viewed_Song2.length;
                        Uri uri=Uri.parse(Viewed_Song2[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed2_artistname[currentPosition].toString());
                            gaudiotext.setText(Mostviewed2_Audio[currentPosition].toString());
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
                    audioname=Mostviewed2_Audio[position];
                    name=  Mostviewed2_artistname[0];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Viewed_Song2[position]);
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Viewed_Song2[position]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

             /*   if (isplaying){
                    onTrackPause();
                }
                else{
                    onTrackPlay();
                }*/
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    updateSeekBAr();
                    mediaPlayer.start();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    gartisttext.setText(Mostviewed2_artistname[0]);
                    gaudiotext.setText(Mostviewed2_Audio[position]);
                }else {
                    startActivity(new Intent(mostviewed2.this,nointernetactivity.class));
                }

            }

        });


        final File mostviewedimage1;
        try {
            mostviewedimage1 = File.createTempFile("mostv2", "jpg");
            storageReferenceMostviewed2.getFile(mostviewedimage1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(mostviewedimage1.getAbsolutePath());
                            ((ImageView) findViewById(R.id.mostviewimage2)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mostviewed2.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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
    private void prepareAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(mostviewed2.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

    }

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
        tracks.add(new Track("I Used to Love Hip-Hop","Audiobinger",R.drawable.ma1));
        tracks.add(new Track("Isolation","Audiobinger",R.drawable.ma2));
        tracks.add(new Track("Lonely Winter","Audiobinger",R.drawable.ma3));
        tracks.add(new Track("The Block","Audiobinger",R.drawable.ma4));
        tracks.add(new Track("The Upside Down","Audiobinger",R.drawable.ma5));
        tracks.add(new Track("no more missing","Audiobinger",R.drawable.ma6));
        tracks.add(new Track("Introspection","Audiobinger",R.drawable.ma7));
        tracks.add(new Track("kumori","Audiobinger",R.drawable.ma8));
        tracks.add(new Track("throwback to sunshine","Audiobinger",R.drawable.ma9));
        tracks.add(new Track("Homeward","Audiobinger",R.drawable.ma10));
        tracks.add(new Track("Calmness","Audiobinger",R.drawable.ma11));
        tracks.add(new Track("Searching","Audiobinger",R.drawable.ma12));
        tracks.add(new Track("Bahía Blanca","Audiobinger",R.drawable.ma13));
        tracks.add(new Track("Am I Floating","Audiobinger",R.drawable.ma14));
        tracks.add(new Track("afternoon tea","Audiobinger",R.drawable.ma15));
        tracks.add(new Track("T s u n a m i","Audiobinger",R.drawable.ma16));
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
                    startActivity(new Intent(mostviewed2.this,nointernetactivity.class));
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
                    if (null!=info)
                    {
                        mediaPlayer.reset();
                        try {

                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            mediaPlayer.setDataSource(Viewed_Song2[rand_int2]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            shuffle.setVisibility(View.INVISIBLE);
                            //CreateNotification.createNotification(mostviewed2.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed2_artistname[0]);
                            gaudiotext.setText(Mostviewed2_Audio[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(mostviewed2.this,nointernetactivity.class));
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

                            unregisterReceiver(noisyAudioStreamreceiver);
                            shuffle.setVisibility(View.INVISIBLE);
                            mediaPlayer.setDataSource(Viewed_Song2[rand_int1]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            registerReceiver(noisyAudioStreamreceiver,intentFilter);
                            //CreateNotification.createNotification(mostviewed2.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Mostviewed2_artistname[0]);
                            gaudiotext.setText(Mostviewed2_Audio[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(mostviewed2.this,nointernetactivity.class));
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
        CreateNotification.createNotification(mostviewed2.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(mostviewed2.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
        String Mostviewed2_Audio[];
        String Mostviewed2_Artistname[];
        int Mostviewed2_images[];


        public MyAdapter(@NonNull Context c, String songname[], String artistname[], int images[]) {
            super(c, R.layout.tuckerlistitem, R.id.tuckeraudioname, songname);
            this.context = c;
            this.Mostviewed2_Audio = songname;
            this.Mostviewed2_Artistname = artistname;
            this.Mostviewed2_images = images;
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
            songname.setText(Mostviewed2_Audio[position].toString());
            imageView.setImageResource(Mostviewed2_images[position]);
            artistname.setText(Mostviewed2_Artistname[0].toString());
            return listitem;
        }
    }
}