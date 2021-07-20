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

public class BootlegBoy extends AppCompatActivity implements View.OnClickListener,Playable{
    ImageView bootlegmainimage;
    private static final String TAG = "BootlegBoy";
    private InterstitialAd mInterstitialAd;
    MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    List<Track> tracks;
    AudioManager audioManager;
    private NoisyAudioStreamreceiver noisyAudioStreamreceiver;
    private IntentFilter intentFilter;
    NotificationManager notificationManager;
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
    private BottomSheetBehavior mbottomsheetBehavior;
    ImageView play;
    ImageView pause;
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
    ImageView imageView11;
    ImageView imageView12;
    ImageView imageView13;
    ImageView imageView14;
    ImageView imageView15;
    private StorageReference storageReferenceBootleg;
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
    private StorageReference storageReference11;
    private StorageReference storageReference12;
    private StorageReference storageReference13;
    private StorageReference storageReference14;
    private StorageReference storageReference15;
    ArrayList<String> songs=new ArrayList<String>();
    ArrayList<String> songname=new ArrayList<String>();
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
            //notificationManager.cancelAll();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bootleg_boy);
        View  bottomsheet=findViewById(R.id.bottom_sheet_player);
        bottomshettplayer=(ConstraintLayout) findViewById(R.id.bottom_sheet_player);
        mbottomsheetBehavior= BottomSheetBehavior.from(bottomsheet);
        gaudiotext=findViewById(R.id.generalaudioname);
        gartisttext=findViewById(R.id.generalartistname);
        bootlegmainimage=findViewById(R.id.bootlegmainimage);
        pause=(ImageView)findViewById(R.id.pause);
        play=(ImageView)findViewById(R.id.play);
        previous=(ImageView)findViewById(R.id.previous);
        next=(ImageView)findViewById(R.id.next);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(2);
        progressDialog.show();
        imageView1=findViewById(R.id.songplaylistb1);
        imageView2=findViewById(R.id.songplaylistb2);
        imageView3=findViewById(R.id.songplaylistb3);
        imageView4=findViewById(R.id.songplaylistb4);
        imageView5=findViewById(R.id.songplaylistb5);
        imageView6=findViewById(R.id.songplaylistb6);
        imageView7=findViewById(R.id.songplaylistb7);
        imageView8=findViewById(R.id.songplaylistb8);
        imageView9=findViewById(R.id.songplaylistb9);
        imageView10=findViewById(R.id.songplaylistb10);
        imageView11=findViewById(R.id.songplaylistb11);
        imageView12=findViewById(R.id.songplaylistb12);
        imageView13=findViewById(R.id.songplaylistb13);
        imageView14=findViewById(R.id.songplaylistb14);
        imageView15=findViewById(R.id.songplaylistb15);
        prepareAD();
        storageReferenceBootleg= FirebaseStorage.getInstance().getReference().child("BootlegBoy/bootlegdisplay.png");
        storageReference1= FirebaseStorage.getInstance().getReference().child("BootlegBoy/alone1.jpg");
        storageReference2= FirebaseStorage.getInstance().getReference().child("BootlegBoy/alone2.jpg");
        storageReference3= FirebaseStorage.getInstance().getReference().child("BootlegBoy/alone3.jpg");
        storageReference4= FirebaseStorage.getInstance().getReference().child("BootlegBoy/alone4.jpg");
        storageReference5= FirebaseStorage.getInstance().getReference().child("BootlegBoy/alone5.jpg");
        storageReference6= FirebaseStorage.getInstance().getReference().child("BootlegBoy/sleep1.jpg");
        storageReference7= FirebaseStorage.getInstance().getReference().child("BootlegBoy/sleep2.jpg");
        storageReference8= FirebaseStorage.getInstance().getReference().child("BootlegBoy/sleep3.jpg");
        storageReference9= FirebaseStorage.getInstance().getReference().child("BootlegBoy/sleep4.jpg");
        storageReference10= FirebaseStorage.getInstance().getReference().child("BootlegBoy/sleep5.jpg");
        storageReference11= FirebaseStorage.getInstance().getReference().child("BootlegBoy/chill1.jpg");
        storageReference12= FirebaseStorage.getInstance().getReference().child("BootlegBoy/chill2.jpg");
        storageReference13= FirebaseStorage.getInstance().getReference().child("BootlegBoy/chill3.jpg");
        storageReference14= FirebaseStorage.getInstance().getReference().child("BootlegBoy/chill4.jpg");
        storageReference15= FirebaseStorage.getInstance().getReference().child("BootlegBoy/chill5.jpg");
        songname.add("Wavesurfer");
        songname.add("N A R U T O V I B E S");
        songname.add("Early Bright Cheers");
        songname.add("staying in");
        songname.add("At Cafe lofi jazz");
        songname.add("Shiloh Dynasty");
        songname.add("Steven Universe Lofi");
        songname.add("Chillhop Yearmix 2018");
        songname.add("Chill Jazz");
        songname.add("Waiting for Train");
        songname.add("it's you in my heart");
        songname.add("듣기 좋은 노래");
        songname.add("chill aesthetic music");
        songname.add("Christmas is Cold!");
        songname.add("La mejor lofi");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fyt1s.com%20-%202%20Hours%20of%20Chill%20%20Sad%20Music%20%20Shiloh%20Dynasty%20Lofi%20Hiphop%20Jazzhop%20study%20work%20sleep%20homework.mp3?alt=media&token=fe84ba95-5dc6-4c71-b84a-e59e149bb316");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fyt1s.com%20-%20Steven%20Universe%20Lofi%20Theme%20%202%20Hour%20Edition.mp3?alt=media&token=9c648d92-89fd-4a23-b7fa-3958d76a305a");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fyt1s.com%20-%20Chillhop%20Yearmix%202018%20%20%20chillhop%20%20lofi%20hip%20hop.mp3?alt=media&token=4e543da4-38b3-4354-95d5-2941ad310922");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fyt1s.com%20-%20Chill%20Study%20Beats%204%20%20jazz%20%20lofi%20hiphop%20Mix%202017.mp3?alt=media&token=89258b78-848b-4d42-8899-252d18c21b70");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fyt1s.com%20-%20waiting%20for%20the%20train%20%202%20hours%20of%20lofi%20beats%20to%20chill%20to.mp3?alt=media&token=4940a091-2e3f-48b9-9e51-466393fa0899");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fyt1s.com%20-%203%20Hour%20%20Lofi%20HipHop%20Chill%20Music%20for%20Stress%20Relief%20and%20Relaxing%20%20its%20you%20in%20my%20heart.mp3?alt=media&token=4bca7b03-e768-4fd5-a64b-5c0e0c9a4ad6");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fyt1s.com%20-%20%20%F0%9D%91%B7%F0%9D%92%8D%F0%9D%92%82%F0%9D%92%9A%F0%9D%92%8D%F0%9D%92%8A%F0%9D%92%94%F0%9D%92%95%20%20%EC%BD%94%EB%94%A9%ED%95%A0%EB%95%8C%20%EB%93%A3%EA%B8%B0%20%EC%A2%8B%EC%9D%80%20%EB%85%B8%EB%9E%98%20%203%20hour%20playlist%20%20Lofi%20hip%20hop%20mix%20%20jazzhop%20%20relax%20beats.mp3?alt=media&token=e8a8e3c9-a04e-457b-8d73-a5cced68ff7f");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fyt1s.com%20-lofi%20hip%20hop%203%20hours%20of%20chill%20aesthetic%20music.mp3?alt=media&token=04609656-8263-4a97-89e6-667a08b7f3a6");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fyt1s.com%20-%20LOFI%20CHILL%20HOP%20STYLE%20%203%20HOUR%20LONG%20Study%20Music%20for%20Memory%20and%20Focus%20%20Christmas%20is%20Cold%20without%20You.mp3?alt=media&token=870ddfbb-0c72-4fdd-89fc-0a7521fd1431");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fyt1s.com%20-%203%20Hours%20Relaxing%20Sleep%20Music%20%20Meditation%20Music%20Stress%20Relief%20MusicMusic%20For%20Study%20Sounds%20of%20Rain.mp3?alt=media&token=365d9f19-ecb1-4487-bd17-50cf60032cf7");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2FWavesurfer%20-%20A%20Chillwave%20Mix.mp3?alt=media&token=168297ec-ff57-41fa-a601-7ca491a5d8ae");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2FN%20A%20R%20U%20T%20O%20V%20I%20B%20E%20S.mp3?alt=media&token=26fd0881-1726-4c3d-b661-c8186075ef26");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2FEarly%20Bright%20Cheers%20(Upbeat%20Lo-fi%20Hip%20Hop%20Mix).mp3?alt=media&token=e956d06f-d57a-444b-b1ae-7463e6bb2c28");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2Fstaying%20in.%20%5Blofi%20jazzhop%20chill%20mix%5D.mp3?alt=media&token=0cc387c7-0364-4b65-94cd-fbdae424f1cc");
        songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/BootlegBoy%2FAt%20Cafe%20lofi%20jazz%20hip%20hop%20mix.mp3?alt=media&token=a632174c-c922-4fb6-8e95-5f27c9cd1ed6");
        final File bootlegmain;
        final File alone1;
        final File alone2;
        final File alone3;
        final File alone4;
        final File alone5;
        final File sleep1;
        final File sleep2;
        final File sleep3;
        final File sleep4;
        final File sleep5;
        final File chill1;
        final File chill2;
        final File chill3;
        final File chill4;
        final File chill5;
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
                    //onTrackPause();
                    audioManager.abandonAudioFocus(afChangeListener);
                }
            }
        };
        if (mediaPlayer.isPlaying())
        {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.INVISIBLE);
            gartisttext.setText("BootlegBoy");
            gaudiotext.setText(songname.get(currentPosition).toString());
            bottomshettplayer.setVisibility(View.VISIBLE);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bottomshettplayer.setVisibility(View.INVISIBLE);
               // notificationManager.cancelAll();
                Toast.makeText(BootlegBoy.this, "Thanks for listening!", Toast.LENGTH_SHORT).show();
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

        try {
            bootlegmain=File.createTempFile("bootlegdisplay","png");
            storageReferenceBootleg.getFile(bootlegmain)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(bootlegmain.getAbsolutePath());
                            ((ImageView)findViewById(R.id.bootlegmainimage)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            alone1=File.createTempFile("alone1","jpg");
            storageReference1.getFile(alone1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap1= BitmapFactory.decodeFile(alone1.getAbsolutePath());
                            imageView1=findViewById(R.id.songplaylistb1);
                            imageView1.setImageBitmap(bitmap1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            alone2=File.createTempFile("alone2","jpg");
            storageReference2.getFile(alone2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap2= BitmapFactory.decodeFile(alone2.getAbsolutePath());
                            imageView2=findViewById(R.id.songplaylistb2);
                            imageView2.setImageBitmap(bitmap2);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            alone3=File.createTempFile("alone3","jpg");
            storageReference3.getFile(alone3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap3= BitmapFactory.decodeFile(alone3.getAbsolutePath());
                            imageView3=findViewById(R.id.songplaylistb3);
                            imageView3.setImageBitmap(bitmap3);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            alone4=File.createTempFile("alone4","jpg");
            storageReference4.getFile(alone4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap4= BitmapFactory.decodeFile(alone4.getAbsolutePath());
                            imageView4=findViewById(R.id.songplaylistb4);
                            imageView4.setImageBitmap(bitmap4);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            alone5=File.createTempFile("alone5","jpg");
            storageReference5.getFile(alone5)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap5= BitmapFactory.decodeFile(alone5.getAbsolutePath());
                            imageView5=findViewById(R.id.songplaylistb5);
                            imageView5.setImageBitmap(bitmap5);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep1=File.createTempFile("sleep1","jpg");
            storageReference6.getFile(sleep1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap6= BitmapFactory.decodeFile(sleep1.getAbsolutePath());
                            imageView6=findViewById(R.id.songplaylistb6);
                            imageView6.setImageBitmap(bitmap6);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep2=File.createTempFile("sleep2","jpg");
            storageReference7.getFile(sleep2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap7= BitmapFactory.decodeFile(sleep2.getAbsolutePath());
                            imageView7=findViewById(R.id.songplaylistb7);
                            imageView7.setImageBitmap(bitmap7);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep3=File.createTempFile("sleep3","jpg");
            storageReference8.getFile(sleep3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap8= BitmapFactory.decodeFile(sleep3.getAbsolutePath());
                            imageView8=findViewById(R.id.songplaylistb8);
                            imageView8.setImageBitmap(bitmap8);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep4=File.createTempFile("sleep4","jpg");
            storageReference9.getFile(sleep4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap9= BitmapFactory.decodeFile(sleep4.getAbsolutePath());
                            imageView9=findViewById(R.id.songplaylistb9);
                            imageView9.setImageBitmap(bitmap9);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep5=File.createTempFile("sleep5","jpg");
            storageReference10.getFile(sleep5)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap10= BitmapFactory.decodeFile(sleep5.getAbsolutePath());
                            imageView10=findViewById(R.id.songplaylistb10);
                            imageView10.setImageBitmap(bitmap10);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            chill1=File.createTempFile("chill1","jpg");
            storageReference11.getFile(chill1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap11= BitmapFactory.decodeFile(chill1.getAbsolutePath());
                            imageView11=findViewById(R.id.songplaylistb11);
                            imageView11.setImageBitmap(bitmap11);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            chill2=File.createTempFile("chill2","jpg");
            storageReference12.getFile(chill2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap12= BitmapFactory.decodeFile(chill2.getAbsolutePath());
                            imageView12=findViewById(R.id.songplaylistb12);
                            imageView12.setImageBitmap(bitmap12);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            chill3=File.createTempFile("chill3","jpg");
            storageReference13.getFile(chill3)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap13= BitmapFactory.decodeFile(chill3.getAbsolutePath());
                            imageView13=findViewById(R.id.songplaylistb13);
                            imageView13.setImageBitmap(bitmap13);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            chill4=File.createTempFile("chill4","jpg");
            storageReference14.getFile(chill4)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap14= BitmapFactory.decodeFile(chill4.getAbsolutePath());
                            imageView14=findViewById(R.id.songplaylistb14);
                            imageView14.setImageBitmap(bitmap14);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            chill5=File.createTempFile("chill5","jpg");
            storageReference15.getFile(chill5)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap15= BitmapFactory.decodeFile(chill5.getAbsolutePath());
                            imageView15=findViewById(R.id.songplaylistb15);
                            imageView15.setImageBitmap(bitmap15);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BootlegBoy.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        bootlegmainimage.setOnClickListener(this);
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
        imageView11.setOnClickListener(this);
        imageView12.setOnClickListener(this);
        imageView13.setOnClickListener(this);
        imageView14.setOnClickListener(this);
        imageView15.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);




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


private void AdforoneHour(){
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            if (isplaying && mInterstitialAd != null) {
                mediaPlayer.pause();
                mInterstitialAd.show(BootlegBoy.this);
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
    private void prepareAD(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(BootlegBoy.this,"ca-app-pub-2470688793006998/8361798413", adRequest, new InterstitialAdLoadCallback() {
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
        tracks.add(new Track("Wavesurfer","1 Hour",R.drawable.alone1));
        tracks.add(new Track("N A R U T O V I B E S","1 Hour",R.drawable.alone2));
        tracks.add(new Track("Early Bright Cheers","1 Hour",R.drawable.alone3));
        tracks.add(new Track("staying in","1 Hour",R.drawable.alone4));
        tracks.add(new Track("At Cafe lofi jazz","1 Hour",R.drawable.alone5));
        tracks.add(new Track("Shiloh Dynasty","2 Hour",R.drawable.sleep1));
        tracks.add(new Track("Steven Universe Lofi","2 Hour",R.drawable.sleep2));
        tracks.add(new Track("Chillhop Yearmix 2018","2 Hour",R.drawable.sleep3));
        tracks.add(new Track("Chill Jazz","2 Hour",R.drawable.sleep4));
        tracks.add(new Track("Waiting for Train","2 Hour",R.drawable.sleep5));
        tracks.add(new Track("it's you in my heart","3 Hour",R.drawable.chill1));
        tracks.add(new Track("듣기 좋은 노래","3 Hour",R.drawable.chill2));
        tracks.add(new Track("chill aesthetic music","3 Hour",R.drawable.chill3));
        tracks.add(new Track("Christmas is Cold!","3 Hour",R.drawable.chill4));
        tracks.add(new Track("La mejor lofi","3 Hour",R.drawable.chill5));
    }

    private void updateSeekBAr() {
        if (mediaPlayer.isPlaying())
        {
            Scrubber.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())*100));
            handler.postDelayed(updater,1000);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isplaying=false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.previous:
                try {
                    if (mediaPlayer.isPlaying())
                    {
                        int curentpositionprev=songs.indexOf(songs);
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(songs.get(curentpositionprev+1));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        play.setVisibility(View.INVISIBLE);
                        pause.setVisibility(View.VISIBLE);
                        gartisttext.setText("BootlegBoy");
                        gaudiotext.setText(songname.get(curentpositionprev+1));

                    }
                }catch (Exception exception)
                {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.next:
                try {
                    if (mediaPlayer.isPlaying())
                    {
                        int curentposition=songs.indexOf(songs);
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(curentposition+1));
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            play.setVisibility(View.INVISIBLE);
                            pause.setVisibility(View.VISIBLE);
                            gartisttext.setText("BootlegBoy");
                            gaudiotext.setText(songname.get(curentposition+1));

                    }
                }catch (Exception exception)
                {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.play:
                ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    isplaying=true;
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }
                else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying())
                {
                    isplaying=false;
                    unregisterReceiver(noisyAudioStreamreceiver);
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
            case R.id.songplaylistb1:
                 manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                 info=manager.getActiveNetworkInfo();
                 if (null!=info){
                     if (null!=info)
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
                         }
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     registerReceiver(noisyAudioStreamreceiver,intentFilter);
                     mediaPlayer.start();
                     updateSeekBAr();
                     pause.setVisibility(View.VISIBLE);
                     play.setVisibility(View.INVISIBLE);
                     bottomshettplayer.setVisibility(View.VISIBLE);
                     gartisttext.setText("1 Hour");
                     gaudiotext.setText(songname.get(currentPosition));
                 }
                 else {
                     startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                 }
                break;

            case R.id.songplaylistb2:
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
                    gartisttext.setText("1 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case R.id.songplaylistb3:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
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
                gartisttext.setText("1 Hour");
                gaudiotext.setText(songname.get(currentPosition));
                break;
            case R.id.songplaylistb4:
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
                    gartisttext.setText("1 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case R.id.songplaylistb5:
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
                    gartisttext.setText("1 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistb6:
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
                    gartisttext.setText("2 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistb7:
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
                    gartisttext.setText("2 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistb8:
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
                    gartisttext.setText("2 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistb9:
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
                    gartisttext.setText("2 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistb10:
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
                    gartisttext.setText("2 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistb11:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=10;
                    isplaying=true;
                    AdforoneHour();
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
                    gartisttext.setText("3 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistb12:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=11;
                    isplaying=true;
                    AdforoneHour();
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
                    gartisttext.setText("3 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistb13:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=12;
                    isplaying=true;
                    AdforoneHour();
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
                    gartisttext.setText("3 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistb14:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=13;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(13));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(13));
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
                    gartisttext.setText("3 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
                }
                break;
            case  R.id.songplaylistb15:
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                info=manager.getActiveNetworkInfo();
                if (null!=info)
                {
                    currentPosition=14;
                    isplaying=true;
                    AdforoneHour();
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(14));
                            mediaPlayer.prepare();

                        }
                        else
                        {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songs.get(14));
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
                    gartisttext.setText("3 Hour");
                    gaudiotext.setText(songname.get(currentPosition));
                }else {
                    startActivity(new Intent(BootlegBoy.this,nointernetactivity.class));
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
        CreateNotification.createNotification(BootlegBoy.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(BootlegBoy.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
}