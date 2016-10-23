package net.devdome.paperplayer.data.model;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:23 PM
 */

public class Song {

    private String title, album, artist, year;

    public Song() {}

    public Song(String title, String album, String artist, String year) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.year = year;
    }
}
