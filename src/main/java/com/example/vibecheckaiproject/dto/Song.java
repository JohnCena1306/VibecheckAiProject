package com.example.vibecheckaiproject.dto;

public class Song {

    private String artist;
    private String title;
    private String reason;

    public Song(String artist, String title, String reason) {
        this.artist = artist;
        this.title = title;
        this.reason = reason;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
