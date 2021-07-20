package com.signupactivity.signupactivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import Services.NotificationActionService;

public class CreateNotification {
    public static final String CHANNEL_ID="channel1";
    public static final String ACTION_PLAY="actionplay";
    public static Notification notification;


    public  static void  createNotification (Context context,Track track,int playbutton ,int position ,int size)
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat=new MediaSessionCompat(context,"tag");
            Bitmap img=BitmapFactory.decodeResource(context.getResources(),track.getImage());

            Intent intentplay=new Intent(context, NotificationActionService.class)
                    .setAction(ACTION_PLAY);
            PendingIntent pendingIntentPlay=PendingIntent.getBroadcast(context,0,
                        intentplay,PendingIntent.FLAG_UPDATE_CURRENT);



            notification=new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                    .setContentTitle(track.getTitle())
                    .setContentText(track.getArtist())
                    .setLargeIcon(img)
                    .addAction(playbutton,"play",pendingIntentPlay)
                   .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                           .setShowActionsInCompactView(0))
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();
            notificationManagerCompat.notify(1,notification);
        }
    }

}
