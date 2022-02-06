package com.music.player.model;

public class MediaItem {

    private String id;
    private String name;
    private String uri;
    private int position;

    public MediaItem(String id, String name, String uri, int position) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.position = position;
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
}
