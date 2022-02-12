package com.music.player.media;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.music.player.model.MediaItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class MediaSource {
    static ContentResolver contentResolver;
    static List<MediaItem> mediaItemListForUi;
    static List<MediaItem> mediaItemList;
    static MediaSource mediaSource = null;
    static MediaItem currentMedia;
    static int currentIndex;


    public static MediaSource getInstance(ContentResolver activity) {
        if (mediaSource == null) {
            contentResolver = activity;
            return new MediaSource();
        }
        return mediaSource;
    }

    private MediaSource() {

    }

    public List<MediaItem> getAllMediaItems() {
        String[] projection = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA};
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Audio.Media.DISPLAY_NAME + " ASC");
        if (Objects.nonNull(cursor) && cursor.getCount() > 0) {

            mediaItemListForUi = new ArrayList<>();
            cursor.moveToFirst();
            int idCol = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int nameCol = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int urlCol = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            while (cursor.moveToNext()) {
                String id = cursor.getString(idCol);
                String name = cursor.getString(nameCol);
                String url = cursor.getString(urlCol);
                mediaItemListForUi.add(new MediaItem(id, name, url, 0, false));
            }
            return mediaItemListForUi;
        }
        return Collections.EMPTY_LIST;
    }

    public void updateBackgroundList(List<MediaItem> mediaItemListForUi) {
        mediaItemList = new ArrayList<>();
        for (MediaItem media : mediaItemListForUi) {
            mediaItemList.add(new MediaItem(media.getId(), media.getName(), media.getUri(), media.getPosition(), media.isPlaying()));
        }


    }

    public List<MediaItem> getAllMedia() {
        return mediaItemListForUi;
    }


    public void setMediaAsCurrent(int position) {
        currentIndex = position;
        currentMedia = mediaItemList.get(position);

    }

    public MediaItem getCurrentMedia() {
        return currentMedia;
    }

    public MediaItem getNextMedia() {
        MediaItem nextMediaItem = mediaItemList.get(++currentIndex);
        setMediaAsCurrent(currentIndex);
        return nextMediaItem;
    }

    public MediaItem getPreviousMedia() {
        MediaItem nextMediaItem = mediaItemList.get(--currentIndex);
        setMediaAsCurrent(currentIndex);
        return nextMediaItem;
    }

}
