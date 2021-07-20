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

public class LongSession3 extends AppCompatActivity implements View.OnClickListener,Playable {
    ImageView chilledcowimage;
    private BottomSheetBehavior mbottomsheetBehavior;
    private static final String TAG = "ChilledCow";
    private InterstitialAd mInterstitialAd;
    private IntentFilter intentFilter;
    private ProgressDialog progressDialog;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    private boolean playingBeforeInterruption=false;
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
        setContentView(R.layout.activity_long_session3);
        chilledcowimage=findViewById(R.id.chilledcowimage);
        View  bottomsheet=findViewById(R.id.bottom_sheet_player);
        bottomshettplayer=(ConstraintLayout) findViewById(R.id.bottom_sheet_player);
      mbottomsheetBehavior=BottomSheetBehavior.from(bottomsheet);
        gaudiotext=findViewById(R.id.generalaudioname);
        gartisttext=findViewById(R.id.generalartistname);
        imageView1=findViewById(R.id.songplaylistl1);
        imageView2=findViewById(R.id.songplaylistl2);
        imageView3=findViewById(R.id.songplaylistl3);
        imageView4=findViewById(R.id.songplaylistl4);
        imageView5=findViewById(R.id.songplaylistl5);
        imageView6=findViewById(R.id.songplaylistl6);
        imageView7=findViewById(R.id.songplaylistl7);
        imageView8=findViewById(R.id.songplaylistl8);
        imageView9=findViewById(R.id.songplaylistl9);
        imageView10=findViewById(R.id.songplaylistl10);
        pause=(ImageView)findViewById(R.id.pause);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(2);
        progressDialog.show();
        play=(ImageView)findViewById(R.id.play);
        prepareAD();
        play.setOnClickListener(this);
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
                        //onTrackPlay();
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
            gartisttext.setText(artistname.get(currentPosition));
            gaudiotext.setText(songname.get(currentPosition).toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bottomshettplayer.setVisibility(View.INVISIBLE);
                //notificationManager.cancelAll();
                Toast.makeText(LongSession3.this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
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
        storagereferenceChilledcow= FirebaseStorage.getInstance().getReference().child("MidnightVibes/mainpage.png");
        storageReference1= FirebaseStorage.getInstance().getReference().child("MidnightVibes/bollywood1.jpg");
        storageReference2= FirebaseStorage.getInstance().getReference().child("MidnightVibes/bollywood2.jpg");
        storageReference3= FirebaseStorage.getInstance().getReference().child("MidnightVibes/bollywood3.jpg");
        storageReference4= FirebaseStorage.getInstance().getReference().child("MidnightVibes/bollywood4.jpg");
        storageReference5= FirebaseStorage.getInstance().getReference().child("MidnightVibes/bollywood5.jpg");
        storageReference6= FirebaseStorage.getInstance().getReference().child("MidnightVibes/ltb1.jpg");
        storageReference7= FirebaseStorage.getInstance().getReference().child("MidnightVibes/ltb2.jpg");
        storageReference8= FirebaseStorage.getInstance().getReference().child("MidnightVibes/ltb3.jpg");
        storageReference9= FirebaseStorage.getInstance().getReference().child("MidnightVibes/ltb4.jpg");
        storageReference10= FirebaseStorage.getInstance().getReference().child("MidnightVibes/ltb5.jpg");
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
        songname.add("Indian lofi/chillfi");
        songname.add("3 AM In Delhi🌃");
        songname.add("Bollywood Lofi💙");
        songname.add("sad days");
        songname.add("Hindi Lofi Chillhop");
        songname.add("Lullaby");
        songname.add("Midnight Vibes");
        songname.add("Midnight Vibes  Lofi");
        songname.add("Good Vibes Only  Summer");
        songname.add("Good Vibes Only");
        artistname.add("Midnight Vibes");
        artistname.add("Anshumaan");
        artistname.add("Anshumaan");
        artistname.add("leftmelonely");
        artistname.add("Music Song Lofi");
        artistname.add("L.T.B");
        artistname.add("L.T.B");
        artistname.add("L.T.B");
        artistname.add("L.T.B");
        artistname.add("L.T.B");

        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/MidnightVibes%2Fyt1s.com%20-%20This%20is%20what%20Indian%20lofichillfi%20sounds%20like%20%201%20hour%20mix%20to%20%20study%20drive%20relax%20and%20chill%20.mp3?alt=media&token=796c797d-4ec5-447d-b406-0cd4c05f220a");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/MidnightVibes%2Fyt1s.com%20-%203%20AM%20In%20Delhi%20%20%20Slow%20and%20Reverb%20Bollywood%20Songs%20you%20can%20Relax%20to.mp3?alt=media&token=daa4daf5-8d6f-4843-a670-bdff1c0c3660");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/MidnightVibes%2Fyt1s.com%20-%20Best%20Of%20Bollywood%20Lofi%20%20%201%20Hour%20Mix%20to%20drive%20study%20relax%20and%20chill%20.mp3?alt=media&token=709acf67-af3d-44bd-8bcd-ab8f5639c738");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/MidnightVibes%2Fyt1s.com%20-%20sad%20songs%20for%20sad%20days%20bollywood%20lofi%20music%20mix.mp3?alt=media&token=8f5159ae-058c-416b-af7d-7c5ca8ec3d1f");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/MidnightVibes%2Fyt1s.com%20-%20Music%20song%20Lofi%20%20Hindi%20Lofi%20Hip%20Hop%20Chillhop%20Music%20Mix.mp3?alt=media&token=356add59-3f2c-4685-af81-7976432dadeb");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Chilledcow%2Fyt1s.com%20-%20Lullaby%20to%20the%20Stars%20%20lofi%20hip%20hop%20%20study%20beats%20.mp3?alt=media&token=dd2e3381-7aef-4b0c-8cf8-cb13acf17844");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/MidnightVibes%2Fyt1s.com%20-%20Midnight%20Vibes%20%20Lofi%20Deep%20House%20Mix.mp3?alt=media&token=82b812dc-8637-472b-b32f-cb5998e81371");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/MidnightVibes%2Fyt1s.com%20-%20Midnight%20Vibes%20%20Lofi%20Deep%20House%20Mix%20Vol2.mp3?alt=media&token=67ee8e7a-be2c-4e51-8ec0-8090f71f91d4");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/MidnightVibes%2Fyt1s.com%20-%20Good%20Vibes%20Only%20%20Summer%20Chill%20Mix.mp3?alt=media&token=c1bae5b7-4f34-4387-9aa7-20cec1ccddf9");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/MidnightVibes%2Fyt1s.com%20-%20Good%20Vibes%20Only%20%20Lofi%20House%20Mix.mp3?alt=media&token=7f9148f0-5413-45b7-a158-22ba1d20d257");

        pause.setOnClickListener(this);


        try {
            chilledowmain=File.createTempFile("mainpage","png");
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
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            study1=File.createTempFile("bollywood1","jpg");
            storageReference1.getFile(study1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap1= BitmapFactory.decodeFile(study1.getAbsolutePath());
                            imageView1=findViewById(R.id.songplaylistl1);
                            imageView1.setImageBitmap(bitmap1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            study2=File.createTempFile("bollywood2","jpg");
            storageReference2.getFile(study2)
                    .addOnSuccessListener   (new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap2= BitmapFactory.decodeFile(study2.getAbsolutePath());
                            imageView2=findViewById(R.id.songplaylistl2);
                            imageView2.setImageBitmap(bitmap2);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            study3=File.createTempFile("bollywood3","jpg");
            storageReference3.getFile(study3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap3= BitmapFactory.decodeFile(study3.getAbsolutePath());
                            imageView3=findViewById(R.id.songplaylistl3);
                            imageView3.setImageBitmap(bitmap3);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            study4=File.createTempFile("bollywood4","jpg");
            storageReference4.getFile(study4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap4= BitmapFactory.decodeFile(study4.getAbsolutePath());
                            imageView4=findViewById(R.id.songplaylistl4);
                            imageView4.setImageBitmap(bitmap4);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            study5=File.createTempFile("bollywood5","jpg");
            storageReference5.getFile(study5)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap5= BitmapFactory.decodeFile(study5.getAbsolutePath());
                            imageView5=findViewById(R.id.songplaylistl5);
                            imageView5.setImageBitmap(bitmap5);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            peace1=File.createTempFile("ltb1","jpg");
            storageReference6.getFile(peace1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap6= BitmapFactory.decodeFile(peace1.getAbsolutePath());
                            imageView6=findViewById(R.id.songplaylistl6);
                            imageView6.setImageBitmap(bitmap6);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
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
                            imageView7=findViewById(R.id.songplaylistl7);
                            imageView7.setImageBitmap(bitmap7);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
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
                            imageView8=findViewById(R.id.songplaylistl8);
                            imageView8.setImageBitmap(bitmap8);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
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
                            imageView9=findViewById(R.id.songplaylistl9);
                            imageView9.setImageBitmap(bitmap9);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
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
                            imageView10=findViewById(R.id.songplaylistl10);
                            imageView10.setImageBitmap(bitmap10);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LongSession3.this, "Error Occured!", Toast.LENGTH_SHORT).show();
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
    /*private void createChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Decibel Inc", NotificationManager.IMPORTANCE_LOW);
            notificationManager=getSystemService(NotificationManager.class);
            if (notificationManager !=null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
*/
    private void populatetrack() {
        tracks=new ArrayList<>();
        tracks.add(new Track("Indian lofi/chillfi","Midnight Vibes",R.drawable.bollywood1));
        tracks.add(new Track("3 AM In Delhi🌃","Anshumaan",R.drawable.bollywood2));
        tracks.add(new Track("Bollywood Lofi💙","Anshumaan",R.drawable.bollywood3));
        tracks.add(new Track("sad days","leftmelonely",R.drawable.bollywood4));
        tracks.add(new Track("Hindi Lofi Chillhop","Music Song Lofi",R.drawable.bollywood5));
        tracks.add(new Track("Lullaby","L.T.B",R.drawable.ltb1));
        tracks.add(new Track("Midnight Vibes","L.T.B",R.drawable.ltb2));
        tracks.add(new Track("Midnight Vibes  Lofi","L.T.B",R.drawable.ltb3));
        tracks.add(new Track("Good Vibes Only  Summer","L.T.B",R.drawable.ltb4));
        tracks.add(new Track("Good Vibes Only","L.T.B",R.drawable.ltb5));
    }

    private void updateSeekBAr() {
        if (mediaPlayer.isPlaying())
        {
            Scrubber.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())*100));
            handler.postDelayed(updater,1000);
        }
    }

    private void prepareAD() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(LongSession3.this,"ca-app-pub-2470688793006998/8361798413", adRequest, new InterstitialAdLoadCallback() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.play:
                ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    isplaying=true;
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    //onTrackPlay();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }else {
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
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
                else {
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
            case R.id.songplaylistl1:
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
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    //onTrackPlay();
                    updateSeekBAr();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
                }
                break;

            case R.id.songplaylistl2:
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
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
                }
                break;
            case R.id.songplaylistl3:
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
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
                }
                break;
            case R.id.songplaylistl4:
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
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
                }
                break;
            case R.id.songplaylistl5:
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
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistl6:
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
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistl7:
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
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistl8:
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
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistl9:
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
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistl10:
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
                }else {
                    startActivity(new Intent(LongSession3.this,nointernetactivity.class));
                }
                break;
        }

    }

    private void AdforoneHour() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isplaying && mInterstitialAd != null) {
                    mediaPlayer.pause();
                    mInterstitialAd.show(LongSession3.this);
                    onTrackPause();
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


    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(LongSession3.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(LongSession3.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
           // notificationManager.cancelAll();
        }
        unregisterReceiver(broadcastReceiver);
    }

    private class NoisyAudioStreamreceiver extends BroadcastReceiver {
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
    }
