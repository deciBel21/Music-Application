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

public class TuckerZone extends AppCompatActivity implements View.OnClickListener,Playable{
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
    ConnectivityManager manager;
    public boolean isplaying=false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying())
        {
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
    String[] Tucker_Audio={"TuckerZone1","TuckerZone2","TuckerZone3","CrystalCode","Another World","Forest Walk"};
    String[] Tucker_artistname={"John tucker","John tucker","John tucker","SkyDance","SkyDance","SkyDance"};
    int Tucker_images[]={R.drawable.audioimage,R.drawable.audioimage,R.drawable.audioimage,R.drawable.skydance,R.drawable.skydance,R.drawable.skydance};

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
        Random rand = new Random();
        Random rand2 = new Random();
        rand_int1=rand.nextInt(Tucker_Audio.length);
        rand_int2=rand2.nextInt(Tucker_Audio.length)+1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tucker_zone);
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
        InterstitialAd.load(TuckerZone.this,"ca-app-pub-3940256099942544/8691691433", adRequest, new InterstitialAdLoadCallback() {
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
           gartisttext.setText(Tucker_artistname[currentPosition].toString());
           gaudiotext.setText(Tucker_Audio[currentPosition].toString());
           bottomshettplayer.setVisibility(View.VISIBLE);
       }
       songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20The%20Tucker%20Zone%20A%203D%20Sound%20Experience%20Wear%20Earphones.mp3?alt=media&token=8cc5f4ca-57e0-4919-9243-1ca283d7756e");
       songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20The%20Tucker%20Zone%202.mp3?alt=media&token=2dd3bdb0-3a66-4c51-96a2-4401dcfa4419");
       songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20The%20Tucker%20Zone%208D%20english.mp3?alt=media&token=7e42007d-e357-4080-9df9-982ffe4fbdc7");
       songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20Crystal%20Code%20%203D%20Sound%20Experience%20wear%20earphones.mp3?alt=media&token=a68be4a6-728a-4737-b7d6-fa71f46b7884");
       songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20Another%20World%20%203D%20Sound%20Experience%20wear%20earphones.mp3?alt=media&token=c71e446d-7658-4621-aa69-c119452d6b40");
       songs.add("https://firebasestorage.googleapis.com/v0/b/test-project-20553.appspot.com/o/Tucker%2Fyt1s.com%20-%20A%20walk%20in%20the%20forest%20%20Immersive%20ambience%20experience.mp3?alt=media&token=b6e48349-6b3f-4ad6-a25c-3f87788c8afc");
       mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
           @Override
           public void onCompletion(MediaPlayer mp) {
               bottomshettplayer.setVisibility(View.INVISIBLE);
              // notificationManager.cancelAll();
               prepareAD();
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
        storageReferenceTucker= FirebaseStorage.getInstance().getReference().child("Tucker/mainimage.jpg");

        tuckerozneaudio=(ListView)findViewById(R.id.mostviewed1list);
        MyAdapter adapter=new MyAdapter(this ,Tucker_Audio,Tucker_artistname,Tucker_images);
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
                          gartisttext.setText(Tucker_artistname[currentPosition].toString());
                          gaudiotext.setText(Tucker_Audio[currentPosition].toString());
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
                           gartisttext.setText(Tucker_artistname[currentPosition].toString());
                           gaudiotext.setText(Tucker_Audio[currentPosition].toString());
                           bottomshettplayer.setVisibility(View.VISIBLE);
                           mediaPlayer.start();
                       }


                           }
                });
                manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info=manager.getActiveNetworkInfo();
                if(null!=info){
                    currentPosition=position;
                    audioname=Tucker_Audio[position];
                    name=  Tucker_artistname[position];
                    bottomshettplayer.setVisibility(View.VISIBLE);
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            try {
                                mediaPlayer.setDataSource(songs.get(position));
                            } catch (Exception e){
                                startActivity(new Intent(TuckerZone.this,nointernetactivity.class));
                            }
                            mediaPlayer.prepare();

                        }
                        else
                        {   mediaPlayer.reset();
                            try {
                                mediaPlayer.setDataSource(songs.get(position));
                            } catch (Exception e){
                                startActivity(new Intent(TuckerZone.this,nointernetactivity.class));
                            }
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //if (isplaying){
                    //onTrackPause();
                    //}
                    //else{
                    //onTrackPlay();
                    //}
                    updateSeekBAr();
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    gartisttext.setText(Tucker_artistname[position]);
                    gaudiotext.setText(Tucker_Audio[position]);


                }else {
                    startActivity(new Intent(TuckerZone.this,nointernetactivity.class));
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
                    Toast.makeText(TuckerZone.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
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

    //private void createChannel() {
       // if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
           // NotificationChannel channel=new NotificationChannel(CreateNotification.CHANNEL_ID,
                    //"Decibel Inc", NotificationManager.IMPORTANCE_LOW);
           // notificationManager=getSystemService(NotificationManager.class);
           // if (notificationManager !=null){
               // notificationManager.createNotificationChannel(channel);
           // }
       // }
   // }

    private void populatetrack() {
        tracks=new ArrayList<>();
        tracks.add(new Track("Audio 1","John Tucker",R.drawable.drive1));
        tracks.add(new Track("Audio 2","John Tucker",R.drawable.drive2));
        tracks.add(new Track("Audio 3","John Tucker",R.drawable.drive3));
        tracks.add(new Track("Audio 4","John Tucker",R.drawable.drive4));
        tracks.add(new Track("Audio 5","John Tucker",R.drawable.drive5));
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
           mInterstitialAd.show(TuckerZone.this);
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
                if(null!=info)
                {
                    registerReceiver(noisyAudioStreamreceiver,intentFilter);
                    mediaPlayer.start();
                    updateSeekBAr();
                    //onTrackPlay();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                }else {
                    startActivity(new Intent(TuckerZone.this,nointernetactivity.class));
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
                   if(null!=info)
                   {
                       mediaPlayer.reset();
                       try {
                           registerReceiver(noisyAudioStreamreceiver,intentFilter);
                           mediaPlayer.setDataSource(songs.get(rand_int2));
                           mediaPlayer.prepare();
                           mediaPlayer.start();
                           shuffle.setVisibility(View.INVISIBLE);
                           updateSeekBAr();
                           // CreateNotification.createNotification(TuckerZone.this,tracks.get(rand_int2),R.drawable.ic_baseline_pause_24,rand_int2,tracks.size()-1);
                           pause.setVisibility(View.VISIBLE);
                           play.setVisibility(View.INVISIBLE);
                           gartisttext.setText(Tucker_artistname[rand_int2]);
                           gaudiotext.setText(Tucker_Audio[rand_int2]);
                           bottomshettplayer.setVisibility(View.VISIBLE);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }else {
                       startActivity(new Intent(TuckerZone.this,nointernetactivity.class));
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
                   if(null!=info)
                   {
                       mediaPlayer.reset();
                       try {

                           registerReceiver(noisyAudioStreamreceiver,intentFilter);
                           shuffle.setVisibility(View.INVISIBLE);
                           mediaPlayer.setDataSource(songs.get(rand_int1));
                           mediaPlayer.prepare();
                           mediaPlayer.start();
                           updateSeekBAr();
                           // CreateNotification.createNotification(TuckerZone.this,tracks.get(rand_int1),R.drawable.ic_baseline_pause_24,rand_int1,tracks.size()-1);
                           pause.setVisibility(View.VISIBLE);
                           play.setVisibility(View.INVISIBLE);
                           gartisttext.setText(Tucker_artistname[rand_int1]);
                           gaudiotext.setText(Tucker_Audio[rand_int1]);
                           bottomshettplayer.setVisibility(View.VISIBLE);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
                   else {
                       startActivity(new Intent(TuckerZone.this,nointernetactivity.class));
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
        CreateNotification.createNotification(TuckerZone.this,tracks.get(currentPosition),R.drawable.ic_baseline_pause_24,currentPosition,tracks.size()-1);
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
            CreateNotification.createNotification(TuckerZone.this,tracks.get(currentPosition),R.drawable.ic_baseline_play_arrow_24,currentPosition,tracks.size()-1);
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
            //            notificationManager.cancelAll();
        unregisterReceiver(broadcastReceiver);
    }

    class MyAdapter extends ArrayAdapter{
        Context context;
        MediaPlayer mediaPlayer;
        String Tucker_Audio[];
        String Tucker_Artistname[];
        int Tucker_images[];



        public MyAdapter(@NonNull Context c, String songname[] ,String artistname[],int images[]) {
            super( c, R.layout.tuckerlistitem,R.id.tuckeraudioname,songname);
            this.context=c;
            this.Tucker_Audio=songname;
            this.Tucker_Artistname=artistname;
            this.Tucker_images=images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listitem=layoutInflater.inflate(R.layout.tuckerlistitem,parent,false);
            ImageView imageView=listitem.findViewById(R.id.audioimage);
             songname=listitem.findViewById(R.id.tuckeraudioname);
            artistname=listitem.findViewById(R.id.tuckeraudioartistname);
            imageView.setImageResource(Tucker_images[position]);
            songname.setText(Tucker_Audio[position].toString());
        artistname.setText(Tucker_Artistname[position].toString());


            return listitem;

        }
    }
}