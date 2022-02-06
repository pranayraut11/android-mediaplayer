package com.music.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.music.player.media.MediaSource;
import com.music.player.media.SongListAdapter;
import com.music.player.model.MediaItem;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, MediaPlayer.OnPreparedListener, View.OnClickListener, MediaPlayer.OnCompletionListener {

    ListView songListView;
    MediaPlayer mediaPlayer;
    MediaSource mediaSource;
    SongListAdapter songListAdapter;
    ImageButton playPauseMediaButton;
    ImageButton nextMediaButton;
    ImageButton previousButton;
    private static int currentPlayingMediaPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songListView = findViewById(R.id.songListView);
        playPauseMediaButton = findViewById(R.id.playPauseButton);
        nextMediaButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        previousButton.setOnClickListener(this);
        nextMediaButton.setOnClickListener(this);
        playPauseMediaButton.setOnClickListener(this);
        mediaSource = new MediaSource(this);
        mediaPlayer = new MediaPlayer();
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        } else {
            initialize();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        } else {
            Toast.makeText(this, "You have given permission to read your storage", Toast.LENGTH_SHORT).show();
            initialize();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MediaItem mediaItem = (MediaItem) view.getTag();
        currentPlayingMediaPosition = i;
        prepareAndPlay(mediaPlayer, mediaItem);
        Toast.makeText(this, "song " + mediaItem.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        playSong();
    }

    void initialize() {
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        songListView.setOnItemClickListener(this);
        songListAdapter = new SongListAdapter(this, mediaSource.getAllMediaItems());
        songListView.setAdapter(songListAdapter);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.nextButton:
                playNextSong();
                break;
            case R.id.previousButton:
                playPreviousSong();
                break;
            case R.id.playPauseButton:
                if (mediaPlayer.isPlaying()) {
                    pauseSong();
                } else {
                    playSong();
                }
                break;
        }

    }

    void playNextSong() {
        incrementPosition();
        MediaItem mediaItem = mediaSource.getMedia(currentPlayingMediaPosition);
        prepareAndPlay(mediaPlayer, mediaItem);
    }

    void playPreviousSong() {
        decrementPosition();
        MediaItem mediaItem = mediaSource.getMedia(currentPlayingMediaPosition);
        prepareAndPlay(mediaPlayer, mediaItem);
    }

    private void prepareAndPlay(MediaPlayer mediaPlayer, MediaItem mediaItem) {
        try {
            mediaPlayer.reset();
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.setDataSource(this, Uri.parse(mediaItem.getUri()));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void incrementPosition() {
        currentPlayingMediaPosition = ++currentPlayingMediaPosition;
    }

    private void decrementPosition() {
        currentPlayingMediaPosition = --currentPlayingMediaPosition;
    }

    private void playSong() {
        playPauseMediaButton.setImageResource(R.drawable.media_pause_image);
        mediaPlayer.start();
        if (!mediaPlayer.isPlaying()) {
            prepareAndPlay(mediaPlayer, mediaSource.getMedia(currentPlayingMediaPosition));
        }
    }

    private void pauseSong() {
        playPauseMediaButton.setImageResource(R.drawable.media_play_image);
        mediaPlayer.pause();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playNextSong();
    }

}