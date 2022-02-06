package com.music.player.media;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;

import com.music.player.model.MediaItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MediaSource {
    Activity activity;
    List<MediaItem> mediaItemList;

    public MediaSource(Activity activity) {
        this.activity = activity;
    }

    public List<MediaItem> getAllMediaItems() {
        String[] projection = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Audio.Media.DISPLAY_NAME + " ASC");
        if (Objects.nonNull(cursor) && cursor.getCount() > 0) {
            mediaItemList = new ArrayList<>();
            cursor.moveToFirst();
            int idCol = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int nameCol = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int urlCol = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            while (cursor.moveToNext()) {
                mediaItemList.add(new MediaItem(cursor.getString(idCol), cursor.getString(nameCol), cursor.getString(urlCol), 0));
            }
            return mediaItemList;
        }
        return Collections.EMPTY_LIST;
    }


    public MediaItem getMedia(int currentPlayingSong) {
        MediaItem nextMedia = null;
        if (Objects.nonNull(mediaItemList) && !mediaItemList.isEmpty()) {
            nextMedia = mediaItemList.parallelStream().filter(mediaItem -> mediaItem.getPosition() == currentPlayingSong).collect(Collectors.toList()).stream().findFirst().get();
        }
        return nextMedia;
    }
}
