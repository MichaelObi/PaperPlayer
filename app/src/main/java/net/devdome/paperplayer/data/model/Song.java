package net.devdome.paperplayer.data.model;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:23 PM
 */

public class Song {

    private String title, album, artist, year;

    private long id = 5;


    public Song() {
    }

    public Song(String title, String album, String artist, String year) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
