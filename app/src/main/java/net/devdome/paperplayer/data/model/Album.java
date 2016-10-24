package net.devdome.paperplayer.data.model;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 4:06 PM
 */

public class Album {

    private final long id;
    private final String name;
    private final String artist;

    public Album(long id, String name, String artist) {

        this.id = id;
        this.name = name;
        this.artist = artist;
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
}
