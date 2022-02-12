package com.music.player.model;

import java.util.Objects;

public class MediaItem {

    private String id;
    private String name;
    private String uri;
    private int position;
    private boolean isPlaying;

    public MediaItem(String id, String name, String uri, int position, boolean isPlaying) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.position = position;
        this.isPlaying = isPlaying;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaItem mediaItem = (MediaItem) o;
        return position == mediaItem.position && Objects.equals(id, mediaItem.id) && Objects.equals(name, mediaItem.name) && Objects.equals(uri, mediaItem.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, uri, position);
    }
}
