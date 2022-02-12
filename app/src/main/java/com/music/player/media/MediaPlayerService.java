package com.music.player.media;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.music.player.MainActivity;
import com.music.player.R;
import com.music.player.model.MediaItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;
    MediaSource mediaSource;
    IBinder iBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSource = MediaSource.getInstance(getContentResolver());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        takeAction(intent.getAction());

        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    private void takeAction(String command) {
        switch (command) {
            case "PLAY":
                mediaPlayer.start();
                break;
            case "PAUSE":
                mediaPlayer.pause();
                break;
            case "NEXT":
                prepareAndPlay(mediaPlayer, mediaSource.getNextMedia());
                break;
            case "PREVIOUS":
                prepareAndPlay(mediaPlayer, mediaSource.getPreviousMedia());
                break;
            case "CLICKED":
                prepareAndPlay(mediaPlayer, mediaSource.getCurrentMedia());
                break;


        }

    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        startForeground(1001, buildNotification().build());
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        prepareAndPlay(mediaPlayer, mediaSource.getNextMedia());
    }


    private void prepareAndPlay(MediaPlayer mediaPlayer, MediaItem mediaItem) {
        try {
            mediaPlayer.reset();
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                stopForeground(true);
            }
            mediaPlayer.setDataSource(this, Uri.parse(mediaItem.getUri()));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private Notification.Builder buildNotification() {
        final String CHANNELID = "Foreground Service ID";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );
        Intent intentMainLanding = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentMainLanding, 0);

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        return new Notification.Builder(this, CHANNELID)
                .setContentText(mediaSource.getCurrentMedia().getName().substring(0,10))
                .setContentTitle("Service enabled")
                .setSmallIcon(R.drawable.ic_launcher_background).setContentIntent(pendingIntent);

    }
}
