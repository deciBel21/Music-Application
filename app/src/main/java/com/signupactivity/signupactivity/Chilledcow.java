package com.signupactivity.signupactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.NotificationManager;
import android.app.ProgressDialog;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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

import Services.OnClearFromRecentService;

public class Chilledcow extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView chilledcowimage;
    private BottomSheetBehavior mbottomsheetBehavior;
    private static final String TAG = "ChilledCow";
    private InterstitialAd mInterstitialAd;
    private IntentFilter intentFilter;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    private boolean playingBeforeInterruption=false;
    private ProgressDialog progressDialog;
    ImageView play;
    ImageView pause;
    MediaPlayer mediaPlayer;
    List<Track> tracks;
    AudioManager audioManager;
    NotificationManager notificationManager;
    int  currentPosition;
    SeekBar Scrubber;
    SeekBar VolumeControl;
    private Handler handler=new Handler();
    int MaxVolume;
    int CurVolume;
    private ConstraintLayout bottomshettplayer;
    private TextView gaudiotext;
    private TextView gartisttext;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;
    ImageView imageView9;
    ImageView imageView10;
    private StorageReference storagereferenceChilledcow;
    private StorageReference storageReference1;
    private StorageReference storageReference2;
    private StorageReference storageReference3;
    private StorageReference storageReference4;
    private StorageReference storageReference5;
    private StorageReference storageReference6;
    private StorageReference storageReference7;
    private StorageReference storageReference8;
    private StorageReference storageReference9;
    private StorageReference storageReference10;
    public boolean isplaying=false;
    ArrayList<String> songs=new ArrayList<String>();
    ArrayList<String> songname=new ArrayList<String>();
    ArrayList<String> artistname=new ArrayList<>();
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying())
        {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chilledcow);
        chilledcowimage=findViewById(R.id.chilledcowimage);
        View  bottomsheet=findViewById(R.id.bottom_sheet_player);
        bottomshettplayer=(ConstraintLayout) findViewById(R.id.bottom_sheet_player);
        mbottomsheetBehavior=BottomSheetBehavior.from(bottomsheet);
        gaudiotext=findViewById(R.id.generalaudioname);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(2);
        progressDialog.show();
        gartisttext=findViewById(R.id.generalartistname);
        imageView1=findViewById(R.id.songplaylistc1);
        imageView2=findViewById(R.id.songplaylistc2);
        imageView3=findViewById(R.id.songplaylistc3);
        imageView4=findViewById(R.id.songplaylistc4);
        imageView5=findViewById(R.id.songplaylistc5);
        imageView6=findViewById(R.id.songplaylistc6);
        imageView7=findViewById(R.id.songplaylistc7);
        imageView8=findViewById(R.id.songplaylistc8);
        imageView9=findViewById(R.id.songplaylistc9);
        imageView10=findViewById(R.id.songplaylistc10);
        pause=(ImageView)findViewById(R.id.pause);
        play=(ImageView)findViewById(R.id.play);
        play.setOnClickListener(this);
        prepareAD();
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
                   // onTrackPause();
                }else if (focusChange==AudioManager.AUDIOFOCUS_GAIN){
                    if (playingBeforeInterruption){
                        mediaPlayer.start();
                        updateSeekBAr();
                        play.setVisibility(View.INVISIBLE);
                        pause.setVisibility(View.VISIBLE);
                      //  onTrackPlay();
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
        if (mediaPlayer.isPlaying())
        {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.INVISIBLE);
            gartisttext.setText(artistname.get(currentPosition).toString());
            gaudiotext.setText(songname.get(currentPosition).toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bottomshettplayer.setVisibility(View.INVISIBLE);
                //notificationManager.cancelAll();
                Toast.makeText(Chilledcow.this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
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

        storagereferenceChilledcow= FirebaseStorage.getInstance().getReference().child("Chilledcow/mainimage.png");
        storageReference1= FirebaseStorage.getInstance().getReference().child("Chilledcow/studied1.jpg");
        storageReference2= FirebaseStorage.getInstance().getReference().child("Chilledcow/studied2.jpg");
        storageReference3= FirebaseStorage.getInstance().getReference().child("Chilledcow/studied3.jpg");
        storageReference4= FirebaseStorage.getInstance().getReference().child("Chilledcow/studied4.jpg");
        storageReference5= FirebaseStorage.getInstance().getReference().child("Chilledcow/studied5.jpg");
        storageReference6= FirebaseStorage.getInstance().getReference().child("Chilledcow/peace1.jpg");
        storageReference7= FirebaseStorage.getInstance().getReference().child("Chilledcow/peace2.jpg");
        storageReference8= FirebaseStorage.getInstance().getReference().child("Chilledcow/peace3.jpg");
        storageReference9= FirebaseStorage.getInstance().getReference().child("Chilledcow/peace4.jpg");
        storageReference10= FirebaseStorage.getInstance().getReference().child("Chilledcow/peace5.jpg");
        final File chilledowmain;
        final File study1;
        final File study2;
        final File study3;
        final File study4;
        final File study5;
        final File peace1;
        final File peace2;
        final File peace3;
        final File peace4;
        final File peace5;
        songname.add("See you space cowboy");
        songname.add("Love hurts.");
        songname.add("Calm Your Anxiety");
        songname.add("RAINING IN ＯＳＡＫＡ");
        songname.add("The answer is in the stars");
        songname.add("Lullaby");
        songname.add("Just take a Breath");
        songname.add("late night");
        songname.add("Guitar mix");
        songname.add("Mellow Vibe");
        artistname.add("tzelun");
        artistname.add("jay Lounge");
        artistname.add("Neotic");
        artistname.add("TRA$SH");
        artistname.add("DREAM");
        artistname.add("Stars");
        artistname.add("Dreamy");
        artistname.add("Unknown!!!");
        artistname.add("Daydreams");
        artistname.add("Medda");

        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-%20See%20you%20space%20cowboy.mp3?alt=media&token=88db7c30-12a6-4317-bb02-7cc9e1390d4a");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-%20side%20streets%20lofi%20%20jazz%20hop%20%20chill%20beats.mp3?alt=media&token=89b89531-5ed3-4b84-be2a-fbd79c6a492c");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-%20Calm%20Your%20Anxiety.mp3?alt=media&token=dc7b4d03-fcbc-4e05-bea8-5c9c5fed83cc");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-%20RAINING%20IN%20%EF%BC%AF%EF%BC%B3%EF%BC%A1%EF%BC%AB%EF%BC%A1%20Lofi%20HipHop.mp3?alt=media&token=260d7d24-53a6-4f9f-a99e-649499fb8e76");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-%20The%20answer%20is%20in%20the%20stars%20%20lofi%20hip%20hop%20mix.mp3?alt=media&token=7d6db4c6-2567-4c51-91bd-72aea4468cd6");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-%20Lullaby%20to%20the%20Stars%20%20lofi%20hip%20hop%20%20study%20beats%20.mp3?alt=media&token=dd2e3381-7aef-4b0c-8cf8-cb13acf17844");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-Just%20take%20a%20Breath%20%20lofi%20hiphop%20mix.mp3?alt=media&token=0d16449f-5a53-4895-9d88-dc3da1fa3d08");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-%20late%20night%20with%20you%20%20lofi%20study%20mix.mp3?alt=media&token=11adac0a-e60a-42b1-a70f-7d2c1e6d6380");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-%20Daydreams%20%20Guitar%20mix%20%20Lofi.mp3?alt=media&token=5e80fbcf-0c0f-4859-b790-da4df601d723");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-%20Mellow%20Vibe%20%20lofi%20study%20mix.mp3?alt=media&token=996d65b8-0d55-406a-9760-2468b733d972");

        pause.setOnClickListener(this);


        try {
            chilledowmain=File.createTempFile("mainimage","png");
storagereferenceChilledcow.getFile(chilledowmain)
        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap= BitmapFactory.decodeFile(chilledowmain.getAbsolutePath());
                ((ImageView)findViewById(R.id.chilledcowimage)).setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
    }
});
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            study1=File.createTempFile("studied1","jpg");
            storageReference1.getFile(study1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap1= BitmapFactory.decodeFile(study1.getAbsolutePath());
                            imageView1=findViewById(R.id.songplaylistc1);
                            imageView1.setImageBitmap(bitmap1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            study2=File.createTempFile("studied2","jpg");
            storageReference2.getFile(study2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap2= BitmapFactory.decodeFile(study2.getAbsolutePath());
                            imageView2=findViewById(R.id.songplaylistc2);
                            imageView2.setImageBitmap(bitmap2);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            study3=File.createTempFile("studied3","jpg");
            storageReference3.getFile(study3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap3= BitmapFactory.decodeFile(study3.getAbsolutePath());
                            imageView3=findViewById(R.id.songplaylistc3);
                            imageView3.setImageBitmap(bitmap3);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            study4=File.createTempFile("studied4","jpg");
            storageReference4.getFile(study4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap4= BitmapFactory.decodeFile(study4.getAbsolutePath());
                            imageView4=findViewById(R.id.songplaylistc4);
                            imageView4.setImageBitmap(bitmap4);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            study5=File.createTempFile("studied5","jpg");
            storageReference5.getFile(study5)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap5= BitmapFactory.decodeFile(study5.getAbsolutePath());
                            imageView5=findViewById(R.id.songplaylistc5);
                            imageView5.setImageBitmap(bitmap5);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            peace1=File.createTempFile("peace1","jpg");
            storageReference6.getFile(peace1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap6= BitmapFactory.decodeFile(peace1.getAbsolutePath());
                            imageView6=findViewById(R.id.songplaylistc6);
                            imageView6.setImageBitmap(bitmap6);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            peace2=File.createTempFile("peace2","jpg");
            storageReference7.getFile(peace2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap7= BitmapFactory.decodeFile(peace2.getAbsolutePath());
                            imageView7=findViewById(R.id.songplaylistc7);
                            imageView7.setImageBitmap(bitmap7);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            peace3=File.createTempFile("peace3","jpg");
            storageReference8.getFile(peace3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap8= BitmapFactory.decodeFile(peace3.getAbsolutePath());
                            imageView8=findViewById(R.id.songplaylistc8);
                            imageView8.setImageBitmap(bitmap8);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            peace4=File.createTempFile("peace4","jpg");
            storageReference9.getFile(peace4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap9= BitmapFactory.decodeFile(peace4.getAbsolutePath());
                            imageView9=findViewById(R.id.songplaylistc9);
                            imageView9.setImageBitmap(bitmap9);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            peace5=File.createTempFile("peace5","jpg");
            storageReference10.getFile(peace5)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap10= BitmapFactory.decodeFile(peace5.getAbsolutePath());
                            imageView10=findViewById(R.id.songplaylistc10);
                            imageView10.setImageBitmap(bitmap10);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Chilledcow.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        chilledcowimage.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        imageView8.setOnClickListener(this);
        imageView9.setOnClickListener(this);
        imageView10.setOnClickListener(this);
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
    private void AdforoneHour(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isplaying && mInterstitialAd != null) {
                    mediaPlayer.pause();
                    mInterstitialAd.show(Chilledcow.this);
                    //onTrackPause();
                    handler.removeCallbacks(updater);
                    pause.setVisibility(View.INVISIBLE);
                    play.setVisibility(View.VISIBLE);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                prepareAD();

            }
        }, 1); //Timer is in ms here.

    }
    private void prepareAD(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(Chilledcow.this,"ca-app-pub-2470688793006998/8361798413", adRequest, new InterstitialAdLoadCallback() {
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
        tracks.add(new Track("See you space cowboy","tzelun",R.drawable.studied1));
        tracks.add(new Track("Love hurts.","jay Lounge",R.drawable.studied2));
        tracks.add(new Track("Calm Your Anxiety","Neotic",R.drawable.studied3));
        tracks.add(new Track("RAINING IN ＯＳＡＫＡ","TRA$SH",R.drawable.studied4));
        tracks.add(new Track("The answer is in the stars","DREAM",R.drawable.studied5));
        tracks.add(new Track("Lullaby","Stars",R.drawable.peace1));
        tracks.add(new Track("Just take a Breath","Dreamy",R.drawable.peace2));
        tracks.add(new Track("late night","Unknown!!!",R.drawable.peace3));
        tracks.add(new Track("Guitar mix","Daydreams",R.drawable.peace4));
        tracks.add(new Track("Mellow Vibe","Medda",R.drawable.peace5));

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
                    mediaPlayer.start();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    updateSeekBAr();
                    // onTrackPlay();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }
                else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
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
                else {
                    mediaPlayer.reset();
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
            case R.id.songplaylistc1:
                 manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                 info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=0;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(0));
                            mediaPlayer.prepare();
                            if (isplaying){
                                onTrackPause();
                            }
                            else{
                                onTrackPlay();
                            }

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(0));
                            mediaPlayer.prepare();
                            if (isplaying){
                                onTrackPause();
                            }
                            else{
                                onTrackPlay();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
                }
                break;
            case R.id.songplaylistc2:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=1;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(1));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(1));
                            mediaPlayer.prepare();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }
                    mediaPlayer.start();
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
                }
                break;
            case R.id.songplaylistc3:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=2;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(2));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(2));
                            mediaPlayer.prepare();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }
                    mediaPlayer.start();
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
                }
                break;
            case R.id.songplaylistc4:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=3;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(3));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(3));
                            mediaPlayer.prepare();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }
                    mediaPlayer.start();
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
                }
                break;
            case R.id.songplaylistc5:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=4;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(4));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(4));
                            mediaPlayer.prepare();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }
                    mediaPlayer.start();
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
                }
                break;
            case R.id.songplaylistc6:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=5;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(5));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(5));
                            mediaPlayer.prepare();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }
                    mediaPlayer.start();
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistc7:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=6;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(6));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(6));
                            mediaPlayer.prepare();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }
                    mediaPlayer.start();
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistc8:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=7;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(7));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(7));
                            mediaPlayer.prepare();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }
                    mediaPlayer.start();
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistc9:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=8;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(8));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(8));
                            mediaPlayer.prepare();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }
                    mediaPlayer.start();
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistc10:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=9;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(9));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(9));
                            mediaPlayer.prepare();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isplaying){
                        onTrackPause();
                    }
                    else{
                        onTrackPlay();
                    }
                    mediaPlayer.start();
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }
                else {
                    startActivity(new Intent(Chilledcow.this,nointernetactivity.class));
                }
                break;
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
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
                notificationManager.cancelAll();
                handler.removeCallbacks(updater);
            }
        }
    }




    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(Chilledcow.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(Chilledcow.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
        if (Build.VERSION.SDK_INT <=Build.VERSION_CODES.O){
            //notificationManager.cancelAll();
        }
        unregisterReceiver(broadcastReceiver);
    }

}