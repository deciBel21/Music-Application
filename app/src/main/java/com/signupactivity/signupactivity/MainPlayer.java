package com.signupactivity.signupactivity;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Services.OnClearFromRecentService;

public class MainPlayer extends AppCompatActivity implements View.OnClickListener,Playable {
    private BottomSheetBehavior mbottomsheetBehavior;
    MediaPlayer mediaPlayer;
    List<Track> tracks;
    AudioManager audioManager;
    private ProgressDialog progressDialog;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    private static final String TAG = "Mainplayer";
    NotificationManager notificationManager;
    private InterstitialAd mInterstitialAd;
    AudioManager.OnAudioFocusChangeListener afChangeListener;
    private boolean playingBeforeInterruption=false;
    int  currentPosition;
    SeekBar Scrubber;
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
    ImageView settings1;
    ImageView tuckerimage1;
    TextView mainname;
    String userId;
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
    ImageView imageView11;
    ImageView imageView12;
    ImageView imageView13;
    ImageView imageView14;
    ImageView imageView15;
    ImageView imageView16;
    ImageView imageView17;
    ImageView imageView18;
    ImageView imageView19;
    ImageView imageView20;
    ImageView imageView21;
    ImageView imageView22;
    ImageView imageView23;
    ImageView imageView24;
    ImageView imageView25;
    ImageView imageView26;
    ImageView imageView27;
    ImageView imageView28;
    ImageView imageView29;
    ImageView imageView30;
    ImageView imageView31;


    private StorageReference storageReference;
    private StorageReference storageReference1;
    private StorageReference storageReference2;
    private StorageReference storageReference3;
    private StorageReference storageReference4;
    private StorageReference storageReference5;
    private StorageReference storageReference7;
    private StorageReference storageReference8;
    private StorageReference storageReference9;
    private StorageReference storageReference10;
    private StorageReference storageReference11;
    private StorageReference storageReference12;
    private StorageReference storageReference13;
    private StorageReference storageReference14;
    private StorageReference storageReference15;
    private StorageReference storageReference16;
    private StorageReference storageReference17;
    private StorageReference storageReference18;
    private StorageReference storageReference19;
    private StorageReference storageReference20;
    private StorageReference storageReference21;
    private StorageReference storageReference22;
    private StorageReference storageReference23;
    private StorageReference storageReference24;
    private StorageReference storageReference25;
    private StorageReference storageReference26;
    private StorageReference storageReference27;
    private StorageReference storageReference28;
    private StorageReference storageReference29;
    private StorageReference storageReference30;
    private StorageReference storageReference31;
    private FirebaseUser user;
    Boolean yourBool;
    ArrayList<String> artistname=new ArrayList<>();
    ArrayList<String> songs=new ArrayList<String>();
    ArrayList<String> songname=new ArrayList<String>();
    Intent intent=getIntent();
    public boolean isplaying=false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying())
        {
            Toast.makeText(this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            bottomshettplayer.setVisibility(View.INVISIBLE);
            notificationManager.cancelAll();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        setContentView(R.layout.activity_main_player);
        mediaPlayer=new MediaPlayer();
        View  bottomsheet=findViewById(R.id.bottom_sheet_player);
        bottomshettplayer=(ConstraintLayout) findViewById(R.id.bottom_sheet_player);
        mbottomsheetBehavior=BottomSheetBehavior.from(bottomsheet);
        gaudiotext=findViewById(R.id.generalaudioname);
        gartisttext=findViewById(R.id.generalartistname);
        //settings1 = (ImageView) findViewById(R.id.Setting4);
        //mainname = (TextView) findViewById(R.id.mainnn);
        tuckerimage1=(ImageView)findViewById(R.id.tuckerimgae1);
        pause=(ImageView)findViewById(R.id.pause);
        play=(ImageView)findViewById(R.id.play);
        imageView1=findViewById(R.id.songplaylist1);
        imageView2=findViewById(R.id.songplaylist2);
        imageView3=findViewById(R.id.songplaylist3);
        imageView4=findViewById(R.id.songplaylist4);
        imageView5=findViewById(R.id.songplaylist5);
        imageView7=findViewById(R.id.songplaylist7);
        imageView8=findViewById(R.id.songplaylist8);
        imageView9=findViewById(R.id.songplaylist9);
        imageView10=findViewById(R.id.songplaylist10);
        imageView11=findViewById(R.id.songplaylist11);
        imageView12=findViewById(R.id.songplaylist12);
        imageView13=findViewById(R.id.songplaylist13);
        imageView14=findViewById(R.id.songplaylist14);
        imageView15=findViewById(R.id.songplaylist15);
        imageView16=findViewById(R.id.songplaylist16);
        imageView17=findViewById(R.id.songplaylist17);
        imageView18=findViewById(R.id.songplaylist18);
        imageView19=findViewById(R.id.songplaylist19);
        imageView20=findViewById(R.id.songplaylist20);
        imageView21=findViewById(R.id.songplaylist21);
        imageView22=findViewById(R.id.songplaylist22);
        imageView23=findViewById(R.id.songplaylist23);
        imageView24=findViewById(R.id.songplaylist24);
        imageView25=findViewById(R.id.songplaylist25);
        imageView26=findViewById(R.id.songplaylist26);
        imageView27=findViewById(R.id.songplaylist27);
        imageView28=findViewById(R.id.songplaylist28);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(2);
        progressDialog.show();
        imageView29=findViewById(R.id.songplaylist29);
        imageView30=findViewById(R.id.songplaylist30);
        imageView31=findViewById(R.id.songplaylist31);
        previous=(ImageView)findViewById(R.id.previous);
        next=(ImageView)findViewById(R.id.next);
        prepareAd();
        intentFilter=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
     noisyAudioStreamreceiver=new NoisyAudioStreamreceiver();
        artistname.add("Daniel Birch");
        artistname.add("Alex-productions");
        artistname.add("FSM-team");
        artistname.add("Barradeen");
        artistname.add("FSM-team");
        artistname.add("Purrple-cat");
        artistname.add("Purrple-cat");
        artistname.add("Peyton-ross");
        artistname.add("FSM-team");
        artistname.add("Smooth Sounds");
        artistname.add("Unknown!!!");
        artistname.add("GameChops");
        artistname.add("Unknowm!!!");
        songname.add("Waves Of Indigo");
        songname.add("Hip-Hop-vlog-music");
        songname.add("Ambivert");
        songname.add("Rainy-mood");
        songname.add("Cyber-thriller");
        songname.add("Gentle-breeze");
        songname.add("Floating-castle");
        songname.add("Lazy-days");
        songname.add("Move-on");
        songname.add("Gaming High");
        songname.add("Asian Gaming..");
        songname.add("Zelda & Chill");
        songname.add("GAMeVibeZ");

        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Added%2FDaniel%20Birch%20-%20Waves%20Of%20Indigo.mp3?alt=media&token=4c1b070b-367b-4289-a2c3-74c16f4046ff");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Added%2Falex-productions-lo-fi-hip-hop-vlog-music.mp3?alt=media&token=265f639f-5d2f-4e2b-ab58-ff846bc3d700");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Added%2Ffsm-team-escp-ambivert.mp3?alt=media&token=0a53d87f-3c8b-4e79-9ef1-b7c401566694");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Added%2Fbarradeen-escp-rainy-mood.mp3?alt=media&token=936426f3-5021-4bf9-bea0-fd598492a021");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Favourites%2Ffsm-team-escp-cyber-thriller.mp3?alt=media&token=5791a7c7-8bd7-4e70-83ac-0ac3a8a4a6fc");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Favourites%2Fpurrple-cat-gentle-breeze.mp3?alt=media&token=ce82a72d-3e55-402b-be1b-6765a4506b37");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Favourites%2Fpurrple-cat-floating-castle.mp3?alt=media&token=d3b30a7f-9a7e-4f40-a9ed-4c06113dbde5");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Favourites%2Fpeyton-ross-lazy-days.mp3?alt=media&token=740fc45c-0595-4093-a3b6-c26794a5e691");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Favourites%2Ffsm-team-past-move-on.mp3?alt=media&token=235fa259-a73f-4bc5-912b-420670d071e6");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2Fvideoplayback.mp3?alt=media&token=7efda6f7-37d5-41a7-89b9-507c1e570772");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2Fvideoplayback%20(1).mp3?alt=media&token=95b2a95d-cf43-4a37-9e3f-0679c05c29e2");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2Fvideoplayback%20(2).mp3?alt=media&token=ca8ce2da-4044-4765-b3a7-b1c2f7a319ef");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Playlist%2Fvideoplayback%20(3).mp3?alt=media&token=a8741338-f1bf-445e-b97e-e224e840b007");
        play.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        VolumeControl=(SeekBar)findViewById(R.id.seekBarVolume);
        Scrubber=(SeekBar)findViewById(R.id.seekBarScrubber);
        mediaPlayer=new MediaPlayer();
        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        MaxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        CurVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        VolumeControl.setMax(MaxVolume);
        VolumeControl.setProgress(CurVolume);
        Intent intent1=getIntent();
        String songname1=intent1.getStringExtra("Audioname");
        String artistname1=intent1.getStringExtra("Artistname");
        SharedPreferences preferences=getSharedPreferences("SongData",MODE_PRIVATE);
        String Audioname=preferences.getString("AudioName","John Tucker");
        String ArtistName =preferences.getString("ArtistName","Song");
        Boolean flag=preferences.getBoolean("Flag",true);
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
                   onTrackPause();
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
                   onTrackPause();
                   audioManager.abandonAudioFocus(afChangeListener);
               }
           }
       };

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bottomshettplayer.setVisibility(View.INVISIBLE);
                notificationManager.cancelAll();
                Toast.makeText(MainPlayer.this, "Thanks for listening!", Toast.LENGTH_SHORT).show();

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
            createChannel();
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



        TextView textView1=findViewById(R.id.artistname1);
        TextView textView2=findViewById(R.id.artistname2);
        TextView textView3=findViewById(R.id.artistname3);
        TextView textView4=findViewById(R.id.artistname4);
        TextView textView5=findViewById(R.id.artistname5);




        user=FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        DatabaseReference  reference =FirebaseDatabase.getInstance().getReference("users").child(userId);
        storageReference= FirebaseStorage.getInstance().getReference().child("Tucker/zoneimage.jpg");
        storageReference1= FirebaseStorage.getInstance().getReference().child("Playlist/Media_Music_Mix.jpg");
        storageReference2= FirebaseStorage.getInstance().getReference().child("Playlist/Quarantine_beats.jpg");
        storageReference3= FirebaseStorage.getInstance().getReference().child("Playlist/nightwalkprivew.jpg");
        storageReference4= FirebaseStorage.getInstance().getReference().child("Playlist/mysticgate.jpg");
        //storageReference5= FirebaseStorage.getInstance().getReference().child("Playlist/selenademo.jpg");
        storageReference7= FirebaseStorage.getInstance().getReference().child("Added/added1.jpg");
        storageReference8= FirebaseStorage.getInstance().getReference().child("Added/added2.jpg");
        storageReference9= FirebaseStorage.getInstance().getReference().child("Added/added3.jpg");
        storageReference10= FirebaseStorage.getInstance().getReference().child("Added/added4.jpg");
        storageReference11= FirebaseStorage.getInstance().getReference().child("Favourites/fav1.jpg");
        storageReference12= FirebaseStorage.getInstance().getReference().child("Favourites/fav2.jpg");
        storageReference13= FirebaseStorage.getInstance().getReference().child("Favourites/fav3.jpg");
        storageReference14= FirebaseStorage.getInstance().getReference().child("Favourites/fav4.jpg");
        storageReference15= FirebaseStorage.getInstance().getReference().child("Favourites/fav5.jpg");
        storageReference16= FirebaseStorage.getInstance().getReference().child("Artists/bootlegboy.png");
        storageReference17= FirebaseStorage.getInstance().getReference().child("Artists/chilledcow.png");
        storageReference18= FirebaseStorage.getInstance().getReference().child("Artists/midnightvibes.png");
        storageReference19= FirebaseStorage.getInstance().getReference().child("Playlist/drive1.jpg");
        storageReference20= FirebaseStorage.getInstance().getReference().child("Playlist/mood2.jpg");
        storageReference21= FirebaseStorage.getInstance().getReference().child("Playlist/mood3.jpg");
        storageReference22= FirebaseStorage.getInstance().getReference().child("Playlist/mood4.jpg");
        storageReference23= FirebaseStorage.getInstance().getReference().child("Playlist/mood5.jpg");
        storageReference23= FirebaseStorage.getInstance().getReference().child("Playlist/mood5.jpg");
        storageReference24= FirebaseStorage.getInstance().getReference().child("Playlist/gaming1.jpg");
        storageReference25= FirebaseStorage.getInstance().getReference().child("Playlist/gaming2.jpg");
        storageReference26= FirebaseStorage.getInstance().getReference().child("Playlist/gaming3.jpg");
        storageReference27= FirebaseStorage.getInstance().getReference().child("Playlist/gaming4.jpg");
        storageReference28= FirebaseStorage.getInstance().getReference().child("Advait/mainimage.jpg");
        storageReference29= FirebaseStorage.getInstance().getReference().child("Anshumaan/mainimage.jpg");
        storageReference30= FirebaseStorage.getInstance().getReference().child("Pandey/mainimage.jpg");
        storageReference31= FirebaseStorage.getInstance().getReference().child("SlowXReverb/mainimage.jpg");

        final File tuckerphoto;
        final File view1;
        final File view2;
        final File view3;
        final File view4;
        //final File selena;
        final File hipop;
        final File lofi1;
        final File lofi;
        final File low;
        final File fav1;
        final File  fav2;
        final File fav3;
        final File fav4;
        final File fav5;
        final File artist1;
        final File artist2;
        final File artist3;
        final File drive1;
        final File  drive2;
        final File drive3;
        final File drive4;
        final File drive5;
        final File  game1;
        final File game2;
        final File game3;
        final File game4;
        final File  lofiartist1;
        final File  lofiartist2;
        final File  lofiartist3;
        final File lofiartist4;
        pause.setOnClickListener(this);



        try {
            tuckerphoto = File.createTempFile("zoneimage","jpg");
            storageReference.getFile(tuckerphoto)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(tuckerphoto.getAbsolutePath());
                            ((ImageView)findViewById(R.id.tuckerimgae1)).setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            view1 = File.createTempFile("Media_Music_Mix","jpg");
            storageReference1.getFile(view1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap1= BitmapFactory.decodeFile(view1.getAbsolutePath());
                            imageView1.setImageBitmap(bitmap1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
           view2 = File.createTempFile("Quarantine_beats","jpg");
            storageReference2.getFile(view2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap2= BitmapFactory.decodeFile(view2.getAbsolutePath());
                            imageView2.setImageBitmap(bitmap2);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            view3 = File.createTempFile("nightwalkprivew","jpg");
            storageReference3.getFile(view3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap3= BitmapFactory.decodeFile(view3.getAbsolutePath());
                            imageView3=findViewById(R.id.songplaylist3);
                            imageView3.setImageBitmap(bitmap3);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            view4 = File.createTempFile("mysticgate","jpg");
            storageReference4.getFile(view4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap4= BitmapFactory.decodeFile(view4.getAbsolutePath());
                        imageView4=findViewById(R.id.songplaylist4);
                            imageView4.setImageBitmap(bitmap4);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            hipop= File.createTempFile("added1","jpg");
            storageReference7.getFile(hipop)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap7= BitmapFactory.decodeFile(hipop.getAbsolutePath());
                       imageView7=findViewById(R.id.songplaylist7);
                            imageView7.setImageBitmap(bitmap7);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            lofi1= File.createTempFile("added2","jpg");
            storageReference8.getFile(lofi1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap8= BitmapFactory.decodeFile(lofi1.getAbsolutePath());
                          imageView8=findViewById(R.id.songplaylist8);
                            imageView8.setImageBitmap(bitmap8);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            lofi= File.createTempFile("added3","jpg");
            storageReference9.getFile(lofi)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap9= BitmapFactory.decodeFile(lofi.getAbsolutePath());
                       imageView9=findViewById(R.id.songplaylist9);
                            imageView9.setImageBitmap(bitmap9);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            low= File.createTempFile("added4","jpg");
            storageReference10.getFile(low)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap10= BitmapFactory.decodeFile(low.getAbsolutePath());
                       imageView10=findViewById(R.id.songplaylist10);
                            imageView10.setImageBitmap(bitmap10);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fav1=File.createTempFile("fav1","jpg");
            storageReference11.getFile(fav1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap11= BitmapFactory.decodeFile(fav1.getAbsolutePath());
                      imageView11=findViewById(R.id.songplaylist11);
                            imageView11.setImageBitmap(bitmap11);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fav2=File.createTempFile("fav2","jpg");
            storageReference12.getFile(fav2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap12= BitmapFactory.decodeFile(fav2.getAbsolutePath());
                             imageView12=findViewById(R.id.songplaylist12);
                            imageView12.setImageBitmap(bitmap12);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fav3=File.createTempFile("fav3","jpg");
            storageReference13.getFile(fav3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap13= BitmapFactory.decodeFile(fav3.getAbsolutePath());
                        imageView13=findViewById(R.id.songplaylist13);
                            imageView13.setImageBitmap(bitmap13);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fav4=File.createTempFile("fav4","jpg");
            storageReference14.getFile(fav4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap14= BitmapFactory.decodeFile(fav4.getAbsolutePath());
                            imageView14=findViewById(R.id.songplaylist14);
                            imageView14.setImageBitmap(bitmap14);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fav5=File.createTempFile("fav5","jpg");
            storageReference15.getFile(fav5)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap15= BitmapFactory.decodeFile(fav5.getAbsolutePath());
                           imageView15=findViewById(R.id.songplaylist15);
                            imageView15.setImageBitmap(bitmap15);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            artist1=File.createTempFile("bootlegboy.","png");
            storageReference16.getFile(artist1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap16= BitmapFactory.decodeFile(artist1.getAbsolutePath());
                       imageView16=findViewById(R.id.songplaylist16);
                            imageView16.setImageBitmap(bitmap16);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            artist2=File.createTempFile("chilledcow","png");
            storageReference17.getFile(artist2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap17= BitmapFactory.decodeFile(artist2.getAbsolutePath());
                         imageView17=findViewById(R.id.songplaylist17);
                            imageView17.setImageBitmap(bitmap17);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            artist3=File.createTempFile("midnightvibes","png");
            storageReference18.getFile(artist3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap18= BitmapFactory.decodeFile(artist3.getAbsolutePath());
                             imageView18=findViewById(R.id.songplaylist18);
                            imageView18.setImageBitmap(bitmap18);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            drive1=File.createTempFile("drive1","jpg");
            storageReference19.getFile(drive1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap19= BitmapFactory.decodeFile(drive1.getAbsolutePath());
                            imageView19=findViewById(R.id.songplaylist19);
                            imageView19.setImageBitmap(bitmap19);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            drive2=File.createTempFile("mood2","jpg");
            storageReference20.getFile(drive2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap20= BitmapFactory.decodeFile(drive2.getAbsolutePath());
                            imageView20=findViewById(R.id.songplaylist20);
                            imageView20.setImageBitmap(bitmap20);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            drive3=File.createTempFile("mood3","jpg");
            storageReference21.getFile(drive3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap21= BitmapFactory.decodeFile(drive3.getAbsolutePath());
                            imageView21=findViewById(R.id.songplaylist21);
                            imageView21.setImageBitmap(bitmap21);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            drive4=File.createTempFile("mood4","jpg");
            storageReference22.getFile(drive4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap22= BitmapFactory.decodeFile(drive4.getAbsolutePath());
                            imageView22=findViewById(R.id.songplaylist22);
                            imageView22.setImageBitmap(bitmap22);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            drive5=File.createTempFile("mood5","jpg");
            storageReference23.getFile(drive5)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap23= BitmapFactory.decodeFile(drive5.getAbsolutePath());
                            imageView23=findViewById(R.id.songplaylist23);
                            imageView23.setImageBitmap(bitmap23);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            game1=File.createTempFile("gaming1","jpg");
            storageReference24.getFile(game1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap24= BitmapFactory.decodeFile(game1.getAbsolutePath());
                            imageView24=findViewById(R.id.songplaylist24);
                            imageView24.setImageBitmap(bitmap24);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            game2=File.createTempFile("gaming2","jpg");
            storageReference25.getFile(game2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap24= BitmapFactory.decodeFile(game2.getAbsolutePath());
                            imageView24=findViewById(R.id.songplaylist25);
                            imageView24.setImageBitmap(bitmap24);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            game3=File.createTempFile("gaming3","jpg");
            storageReference26.getFile(game3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap26= BitmapFactory.decodeFile(game3.getAbsolutePath());
                            imageView26=findViewById(R.id.songplaylist26);
                            imageView26.setImageBitmap(bitmap26);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            game4=File.createTempFile("gaming4","jpg");
            storageReference27.getFile(game4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap27= BitmapFactory.decodeFile(game4.getAbsolutePath());
                            imageView27=findViewById(R.id.songplaylist27);
                            imageView27.setImageBitmap(bitmap27);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            lofiartist1=File.createTempFile("mainimage","jpg");
            storageReference28.getFile(lofiartist1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap28= BitmapFactory.decodeFile(lofiartist1.getAbsolutePath());
                            imageView28=findViewById(R.id.songplaylist28);
                            imageView28.setImageBitmap(bitmap28);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            lofiartist2=File.createTempFile("mainimage","jpg");
            storageReference29.getFile(lofiartist2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap29= BitmapFactory.decodeFile(lofiartist2.getAbsolutePath());
                            imageView29=findViewById(R.id.songplaylist29);
                            imageView29.setImageBitmap(bitmap29);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            lofiartist3=File.createTempFile("mainimage","jpg");
            storageReference30.getFile(lofiartist3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap30= BitmapFactory.decodeFile(lofiartist3.getAbsolutePath());
                            imageView30=findViewById(R.id.songplaylist30);
                            imageView30.setImageBitmap(bitmap30);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            lofiartist4=File.createTempFile("mainimage","jpg");
            storageReference31.getFile(lofiartist4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap31= BitmapFactory.decodeFile(lofiartist4.getAbsolutePath());
                            imageView31=findViewById(R.id.songplaylist31);
                            imageView31.setImageBitmap(bitmap31);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPlayer.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        pause.setOnClickListener(this);
        play.setOnClickListener(this);
        tuckerimage1.setOnClickListener(this);
//        settings1.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        //imageView5.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        imageView8.setOnClickListener(this);
        imageView9.setOnClickListener(this);
        imageView10.setOnClickListener(this);
        imageView11.setOnClickListener(this);
        imageView12.setOnClickListener(this);
        imageView13.setOnClickListener(this);
        imageView14.setOnClickListener(this);
        imageView15.setOnClickListener(this);
        imageView16.setOnClickListener(this);
        imageView17.setOnClickListener(this);
        imageView18.setOnClickListener(this);
        imageView19.setOnClickListener(this);
        imageView20.setOnClickListener(this);
        imageView21.setOnClickListener(this);
        imageView22.setOnClickListener(this);
        imageView23.setOnClickListener(this);
        imageView24.setOnClickListener(this);
        imageView25.setOnClickListener(this);
        imageView26.setOnClickListener(this);
        imageView27.setOnClickListener(this);
        imageView28.setOnClickListener(this);
        imageView29.setOnClickListener(this);
        imageView30.setOnClickListener(this);
        imageView31.setOnClickListener(this);
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



      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              User userprofile=snapshot.getValue(User.class);
              if(userprofile!=null)
              {
                  String name=userprofile.fullName;
                  //mainname.setText("Welcome,"+ name +"!");
              }
              else
              {
                 // mainname.setText("Error!");
              }
          }


          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });


    }
    private  class BackgroundJob extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    private void prepareAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(MainPlayer.this,"ca-app-pub-2470688793006998/8361798413", adRequest, new InterstitialAdLoadCallback() {
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

    private Runnable updater= new Runnable() {
        @Override
        public void run() {
            updateSeekBAr();
            long currentduration=mediaPlayer.getCurrentPosition();
        }
    };
    private void createChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Decibel Inc", NotificationManager.IMPORTANCE_LOW);
            notificationManager=getSystemService(NotificationManager.class);
            if (notificationManager !=null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    private void AdforoneHour(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying() && mInterstitialAd != null) {
                    mediaPlayer.pause();
                    mInterstitialAd.show(MainPlayer.this);
                   // onTrackPause();
                    handler.removeCallbacks(updater);
                    pause.setVisibility(View.INVISIBLE);
                    play.setVisibility(View.VISIBLE);
                } else if (mInterstitialAd !=null ){
                    mInterstitialAd.show(MainPlayer.this);

                }else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                prepareAd();

            }
        }, 1); //Timer is in ms here.

    }

    private void populatetrack() {
        tracks=new ArrayList<>();
        tracks.add(new Track("Waves Of Indigo","Daniel Birch ",R.drawable.added1));
        tracks.add(new Track("Hip-Hop-vlog-music","Alex-productions",R.drawable.added2));
        tracks.add(new Track("Rainy-mood","Barradeen",R.drawable.added3));
        tracks.add(new Track("Ambivert","FSM-team",R.drawable.added4));
        tracks.add(new Track("Cyber-thriller","FSM-team",R.drawable.fav1));
        tracks.add(new Track("Gentle-breeze","Purrple-cat",R.drawable.fav2));
        tracks.add(new Track("Floating-castle","Purrple-cat",R.drawable.fav3));
        tracks.add(new Track("Lazy-days","Peyton-ross",R.drawable.fav4));
        tracks.add(new Track("Move-on","FSM-team",R.drawable.fav5));
        tracks.add(new Track("Gaming High","Smooth Sound",R.drawable.gaming1));
        tracks.add(new Track("Asian Gaming..","Unknown!!!",R.drawable.gaming2));
        tracks.add(new Track("Zelda & Chill","GameChops",R.drawable.gaming3));
        tracks.add(new Track("GAMeVibeZ","Unknown!!!",R.drawable.gaming4));
    }
    private void updateSeekBAr() {
        if (mediaPlayer.isPlaying())
        {
            Scrubber.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())*100));
            handler.postDelayed(updater,1000);
        }
    }


    @CallSuper
    public void onStart() {
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
    public void onClick(View v) {
        switch (v.getId())
        {
            /*case R.id.Setting4:
                startActivity(new Intent(this, Setting_page.class));
                break;*/
            case R.id.tuckerimgae1:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {
                    Toast.makeText(this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, TuckerZone.class));

                }else {
                    startActivity(new Intent(this, TuckerZone.class));
                }
                break;
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
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
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
            case  R.id.songplaylist1:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {

                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, mostviewed1.class));
                }else {
                    startActivity(new Intent(this, mostviewed1.class));
                }
                break;
            case  R.id.songplaylist2:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, mostviewed2.class));
                }else {
                    startActivity(new Intent(this, mostviewed2.class));
                }
                break;
            case  R.id.songplaylist3:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {

                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, mostviewed3.class));
                }else {
                    startActivity(new Intent(this, mostviewed3.class));
                }
                break;
            case  R.id.songplaylist4:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, mostviewed4.class));
                }else {
                    startActivity(new Intent(this, mostviewed4.class));
                }
                break;
            case  R.id.songplaylist7:
                 manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                 info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=0;
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

                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();

                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist8:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=1;
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
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist9:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=2;
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
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist10:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=3;
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
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(3));
                    gaudiotext.setText(songname.get(3));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist11:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=4;
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
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist12:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=5;
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
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist13:
                currentPosition=6;
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
                bottomshettplayer.setVisibility(View.VISIBLE);
                gartisttext.setText(artistname.get(currentPosition));
                gaudiotext.setText(songname.get(currentPosition));
                break;


            case  R.id.songplaylist14:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=7;
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

                /*if (isplaying){
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
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist15:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=8;
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
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist16:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, BootlegBoy.class));
                }else {
                    startActivity(new Intent(this, BootlegBoy.class));
                }
                break;
            case  R.id.songplaylist17:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, Chilledcow.class));
                }else {
                    startActivity(new Intent(this, Chilledcow.class));
                }
                break;
            case  R.id.songplaylist18:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, LongSession3.class));
                }else {
                    startActivity(new Intent(this, LongSession3.class));
                }
                break;
            case  R.id.songplaylist19:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {

                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, driveactivity1.class));
                }else {
                    startActivity(new Intent(this, driveactivity1.class));
                }
                break;
            case  R.id.songplaylist20:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {

                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, driveactivity2.class));
                }else {
                    startActivity(new Intent(this, driveactivity2.class));
                }
                break;
            case  R.id.songplaylist21:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {
                    Toast.makeText(this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, driveactivity3.class));
                }else {
                    startActivity(new Intent(this, driveactivity3.class));
                }
                break;
            case  R.id.songplaylist22:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {
                    Toast.makeText(this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, driveactivity4.class));
                }else {
                    startActivity(new Intent(this, driveactivity4.class));
                }
                break;
            case  R.id.songplaylist23:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, driveactivity5.class));
                }else {
                    startActivity(new Intent(this, driveactivity5.class));
                }
                break;
            case  R.id.songplaylist24:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=9;
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

                /*if (isplaying){
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
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist25:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=10;
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(10));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(10));
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
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist26:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=11;
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(11));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(11));
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

                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylist27:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=12;
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(12));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(12));
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

                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    gartisttext.setText(artistname.get(currentPosition));
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(MainPlayer.this,nointernetactivity.class));
                }
                break;

            case R.id.songplaylist28:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {

                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, Advait.class));
                }else {
                    startActivity(new Intent(this, Advait.class));
                }
                break;

            case R.id.songplaylist29:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {

                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, Anshumaan.class));
                }else {
                    startActivity(new Intent(this, Anshumaan.class));
                }
                break;

            case R.id.songplaylist30:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {

                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, Pandey.class));
                }else {
                    startActivity(new Intent(this, Pandey.class));
                }
                break;
            case R.id.songplaylist31:
                AdforoneHour();
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    bottomshettplayer.setVisibility(View.INVISIBLE);
                    notificationManager.cancelAll();
                    startActivity(new Intent(this, SlowX.class));
                }else {
                    startActivity(new Intent(this, SlowX.class));
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
        CreateNotification.createNotification(MainPlayer.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(MainPlayer.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
}