package com.music.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.music.player.media.MediaPlayerService;
import com.music.player.media.MediaSource;
import com.music.player.media.SongListAdapter;
import com.music.player.model.MediaItem;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView songListView;
    MediaSource mediaSource;
    SongListAdapter songListAdapter;
    ImageButton playPauseMediaButton;
    ImageButton nextMediaButton;
    ImageButton previousButton;
    private static int currentPlayingMediaPosition;
    static boolean isLoadingFirstTime = true;


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
        mediaSource = MediaSource.getInstance(getContentResolver());

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        } else {
            initialize();
        }

    }

    @Override
    protected void onDestroy() {
        stopService(getIntent(""));
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
    protected void onStart() {

        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (isLoadingFirstTime) {
            mediaSource.updateBackgroundList(mediaSource.getAllMedia());
            isLoadingFirstTime = false;
        }
        mediaSource.setMediaAsCurrent(i);

        currentPlayingMediaPosition = i;

        playPauseMediaButton.setImageResource(R.drawable.media_pause_image);
        playPauseMediaButton.setTag("PLAY");
        startSongBackground("CLICKED");
        Toast.makeText(this, "song " , Toast.LENGTH_SHORT).show();
    }

    void initialize() {

        songListView.setOnItemClickListener(this);
        songListAdapter = new SongListAdapter(this, mediaSource.getAllMediaItems());
        songListAdapter.setNotifyOnChange(true);
        songListView.setAdapter(songListAdapter);


    }


    @Override
    public void onClick(View view) {
        String command = null;
        switch (view.getId()) {
            case R.id.nextButton:
                command = "NEXT";
                break;
            case R.id.previousButton:
                command = "PREVIOUS";
                break;
            case R.id.playPauseButton:
                if (playPauseMediaButton.getTag().toString().equalsIgnoreCase("Pause")) {
                    playPauseMediaButton.setImageResource(R.drawable.media_pause_image);
                    command = "PLAY";
                    playPauseMediaButton.setTag("PLAY");
                } else {
                    playPauseMediaButton.setImageResource(R.drawable.media_play_image);
                    command = "PAUSE";
                    playPauseMediaButton.setTag("PAUSE");
                }
                break;
        }
        startSongBackground(command);
    }


    private void startSongBackground(String command) {
        Intent intent = getIntent(command);
        try {
            startService(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Intent getIntent(String command) {
        Intent musicPlayerIntent = new Intent(this, MediaPlayerService.class);
        musicPlayerIntent.setAction(command);
        return musicPlayerIntent;
    }

}