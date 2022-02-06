package com.music.player.media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.music.player.R;
import com.music.player.model.MediaItem;

import java.util.List;

public class SongListAdapter extends ArrayAdapter<MediaItem> {


    TextView name;

    public SongListAdapter(@NonNull Context context, @NonNull List<MediaItem> objects) {
        super(context, R.layout.song_list, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MediaItem mediaItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_list, parent, false);
        }
        name = (TextView) convertView.findViewById(R.id.mediaName);
        name.setText(mediaItem.getName());
        mediaItem.setPosition(position);
        convertView.setTag(mediaItem);
        return convertView;
    }

}
