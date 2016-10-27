package net.devdome.paperplayer.data.model;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 4:06 PM
 */

public class Album {

    private long id;
    private String name;
    private String artist;
    private String artPath;

    public Album() {

    }

    public Album(long id, String name, String artist, String artPath) {

        this.id = id;
        this.name = name;
        this.artist = artist;
        this.artPath = artPath;
    }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getArtPath() {
        return artPath;
    }

    public void setArtPath(String artPath) {
        this.artPath = artPath;
    }
}
