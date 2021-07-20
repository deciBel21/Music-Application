package com.signupactivity.signupactivity;

public class Track {
    private String Title;
    private String Artist;
    private int image;

    public Track(String title, String artist, int image) {
        Title = title;
        Artist = artist;
        this.image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
