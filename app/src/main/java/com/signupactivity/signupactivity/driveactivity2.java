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

public class driveactivity2 extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView driveimage2;
    Random rand2;
    List<Track> tracks;
    private InterstitialAd mInterstitialAd;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private boolean playingBeforeInterruption=false;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    private static final String TAG = "drive2";
    NotificationManager notificationManager;
    int currentPosition;
    ImageView driveimage1;
    private BottomSheetBehavior mbottomsheetBehavior;
    SeekBar Scrubber;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    SeekBar VolumeControl;
    private Handler handler=new Handler();
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
    int MaxVolume;
    int CurVolume;
    public boolean isplaying=false;
    ConnectivityManager manager;

    Button shuffle;
    private StorageReference storageReferenceDrive2;
    TextView songname;
    TextView artistname;

    String[] Drive_Song2={"https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2Flilac%20-%20Lanterns%20%5Blofi%20hip%20hoprelaxing%20beats%5D.mp3?alt=media&token=b215028e-d722-4da2-a7d6-8c74e2c42e3c","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2FMiramare%20x%20Cl%C3%A9ment%20Matrat%20-%20Azure%20Blue.mp3?alt=media&token=7d0f1e09-b6e1-4856-a342-25bc5e6df844","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2FHevi%20x%20Kainbeats%20-%20Drifting%20Away.mp3?alt=media&token=2c318a5f-470c-449c-9e22-2e41282bd0b2","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2FRefeeld%20x%20Project%20AER%20-%20Chance%20Encounter.mp3?alt=media&token=3eeecf71-b159-4a04-89d5-ac6c081f6363","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2Fdryhope%20-%20Kenopsia.mp3?alt=media&token=28fefe56-2730-4956-b883-2d736c03c12f","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2FEugenio%20Izzi%20-%20Feel%20Free%20To%20Imagine.mp3?alt=media&token=9b5911ae-7485-47a4-8987-58cdc786b6f3","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2FLilac%20-%20Florist.mp3?alt=media&token=0e68f483-c8dc-4c5f-a2a5-b58c6558da23","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2FChris%20Mazuera%20-%20Dozing.mp3?alt=media&token=349d8e1c-b667-4093-b1a3-49f83dd55530","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2Fdontcry%20x%20nokiaa%20-%20Odyssey.mp3?alt=media&token=33e74dce-ac5b-4ece-9ed9-d128913caace","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2FSPEECHLESS%20-%20Future%20feelings.mp3?alt=media&token=3e6b37c3-2a53-45fd-a4e7-334750c67e92","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2Fyt1s.com%20-%20%EF%BC%B2%EF%BD%95%EF%BD%8E%EF%BD%81%EF%BD%97%EF%BD%81%EF%BD%99.mp3?alt=media&token=fad576ce-6017-4ad9-ac19-ae7ac79dd243","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2Fyt1s.com%20-%20%EF%BC%A1%EF%BD%8B%EF%BD%89%EF%BD%92%EF%BD%81.mp3?alt=media&token=23b2561d-86e4-4c15-a8e7-cc6bf973e52c","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2Fyt1s.com%20-%20%EF%BC%A3%EF%BD%8C%EF%BD%8F%EF%BD%95%EF%BD%84%EF%BD%85%EF%BD%84%E3%80%80%EF%BC%AD%EF%BD%85%EF%BD%8D%EF%BD%8F%EF%BD%92%EF%BD%89%EF%BD%85%EF%BD%93%E3%80%80%E3%81%82%E3%81%B4%E8%89%B6.mp3?alt=media&token=1c5b8953-5b60-4b7b-84fc-b9a1cf04032b","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2Fyt1s.com%20-%20%EF%BD%96%EF%BD%8F%EF%BD%8E%E3%80%80%E3%83%91%E3%83%8F%E3%82%81.mp3?alt=media&token=5711cec4-89b1-4db3-a057-62d2c8fa245e","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2Fyt1s.com%20-%20Brodd%20%20let%20me%20sleep.mp3?alt=media&token=f7993f09-823d-40ee-b6b0-d84bff93ba5f","https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/mood2%2Fyt1s.com%20-%20Haruko%20%20summer%20nostalgia.mp3?alt=media&token=1216551a-b102-4183-8d0b-1016d1537eae"};
    String[] Drive2_Audio={"lilac","Miramare x Clément","Havi x Kainbeats","Refeeld x Project AER","dryhope","EugenioIzzi","Lilac","Chris Mazuera","dontcry x nokiaa","SPEECHLESS","InYourChill","InYourChill","あぴ艶","パハめ","Brodd","Haruko"};
    String[] Drive2_artistname={"Lanterns","Azure Blue","Drifitng Away","Chance Encounter","Kenopsia","Feel Free to Imagine","Florist","Dozing","Odyssey","Future feelings","Ｒｕｎａｗａｙ","Akira","Ｃlouded Memories","ｖｏｎ","let me sleep","summer nostalgia"};
    int Drive2_images[]={R.drawable.smoke1,R.drawable.smoke2,R.drawable.smoke3,R.drawable.smoke4,R.drawable.smoke5,R.drawable.smoke6,R.drawable.smoke7,R.drawable.smoke8,R.drawable.smoke9,R.drawable.smoke10,R.drawable.smoke11,R.drawable.smoke12,R.drawable.smoke13,R.drawable.smoke14,R.drawable.smoke15,R.drawable.smoke16};

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
        else {
            isplaying=false;
        }
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
        if (isplaying){
            registerReceiver(noisyAudioStreamreceiver,intentFilter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driveactivity2);
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Drive_Song2.length);
        rand_int2=rand2.nextInt(Drive_Song2.length)+1;
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
        CurVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        intentFilter=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        noisyAudioStreamreceiver=new NoisyAudioStreamreceiver();
        VolumeControl.setMax(MaxVolume);
        VolumeControl.setProgress(CurVolume);
        prepareAD();
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
                   // onTrackPause();
                    audioManager.abandonAudioFocus(afChangeListener);
                }
            }
        };
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(driveactivity2.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
            gartisttext.setText(Drive2_Audio[currentPosition].toString());
            gaudiotext.setText(Drive2_artistname[currentPosition].toString());
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

        driveimage2= (ImageView) findViewById(R.id.driveimage2);
        storageReferenceDrive2= FirebaseStorage.getInstance().getReference().child("Playlist/mooded2.jpg");
       MyAdapter adapter=new MyAdapter(this,Drive2_artistname,Drive2_Audio,Drive2_images);
        ListView drivelist2=(ListView)findViewById(R.id.drivelistog2);
        drivelist2.setAdapter(adapter);
        drivelist2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Drive_Song2.length;
                        Uri uri=Uri.parse(Drive_Song2[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive2_artistname[currentPosition].toString());
                            gaudiotext.setText(Drive2_Audio[currentPosition].toString());
                            bottomshettplayer.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                        }
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition=position;
                        currentPosition=(currentPosition+1)%Drive_Song2.length;
                        Uri uri=Uri.parse(Drive_Song2[currentPosition].toString());
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                            next.setVisibility(View.INVISIBLE);
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                            mediaPlayer.start();
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive2_artistname[currentPosition].toString());
                            gaudiotext.setText(Drive2_Audio[currentPosition].toString());
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
                    audioname=Drive2_Audio[position];
                    name=  Drive2_artistname[position];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (isplaying && mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Drive_Song2[position]);
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Drive_Song2[position]);
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
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    isplaying=true;
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    gartisttext.setText(Drive2_Audio[position]);
                    gaudiotext.setText(Drive2_artistname[position]);
                }else {
                    startActivity(new Intent(driveactivity2.this,nointernetactivity.class));
                }

            }

        });




        final File mostviewedimage1;
        try {
            mostviewedimage1=File.createTempFile("mooded2","jpg");
            storageReferenceDrive2.getFile(mostviewedimage1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(mostviewedimage1.getAbsolutePath());
                            ((ImageView)findViewById(R.id.driveimage2)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(driveactivity2.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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
    private void AdforoneHour(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isplaying && mInterstitialAd != null) {
                    mediaPlayer.pause();
                   prepareAD();
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
        tracks.add(new Track("Lanterns","lilac",R.drawable.smoke1));
        tracks.add(new Track("Azure Blue","Miramare x Clément",R.drawable.smoke2));
        tracks.add(new Track("Drifitng Away","Havi x Kainbeats",R.drawable.smoke3));
        tracks.add(new Track("Chance Encounter","Refeeld x Project AER",R.drawable.smoke4));
        tracks.add(new Track("Kenopsia","dryhope",R.drawable.smoke5));
        tracks.add(new Track("Feel Free to Imagine","Eugenio Izzi",R.drawable.smoke6));
        tracks.add(new Track("Florist","Lilac",R.drawable.smoke7));
        tracks.add(new Track("Dozing","Chris Mazuera",R.drawable.smoke8));
        tracks.add(new Track("Odyssey","dontcry x nokiaa",R.drawable.smoke9));
        tracks.add(new Track("Future feelings","SPEECHLESS",R.drawable.smoke10));
        tracks.add(new Track("Ｒｕｎａｗａｙ","InYourChill",R.drawable.smoke11));
        tracks.add(new Track("Akira","InYourChill",R.drawable.smoke12));
        tracks.add(new Track("Ｃlouded Memories","あぴ艶",R.drawable.smoke13));
        tracks.add(new Track("ｖｏｎ","パハめ",R.drawable.smoke14));
        tracks.add(new Track("let me sleep","Brodd",R.drawable.smoke15));
        tracks.add(new Track("summer nostalgia","Haruko",R.drawable.smoke16));
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
            mInterstitialAd.show(driveactivity2.this);
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
                    isplaying=true;
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    // onTrackPlay();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }else {
                    startActivity(new Intent(driveactivity2.this,nointernetactivity.class));
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
                            mediaPlayer.setDataSource(Drive_Song2[rand_int2]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            shuffle.setVisibility(View.INVISIBLE);
                            // CreateNotification.createNotification(driveactivity2.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive2_Audio[rand_int2]);
                            gaudiotext.setText(Drive2_artistname[rand_int2]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(driveactivity2.this,nointernetactivity.class));
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
                            mediaPlayer.setDataSource(Drive_Song2[rand_int1]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            updateSeekBAr();
                            // CreateNotification.createNotification(driveactivity2.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                            pause.setVisibility(View.VISIBLE);
                            play.setVisibility(View.INVISIBLE);
                            gartisttext.setText(Drive2_Audio[rand_int1]);
                            gaudiotext.setText(Drive2_artistname[rand_int1]);
                            bottomshettplayer.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        startActivity(new Intent(driveactivity2.this,nointernetactivity.class));
                    }
                  /*  if (isplaying){
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
        CreateNotification.createNotification(driveactivity2.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(driveactivity2.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
        String Drive2_Audio[];
        String Drive2_Artistname[];
        int Drive2_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.Drive2_Audio=songname;
            this.Drive2_Artistname=artistname;
            this.Drive2_images=images;
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
            songname.setText(Drive2_Audio[position].toString());
            imageView.setImageResource(Drive2_images[position]);
            artistname.setText(Drive2_Artistname[position].toString());
            return listitem;

        }
    }
    }
