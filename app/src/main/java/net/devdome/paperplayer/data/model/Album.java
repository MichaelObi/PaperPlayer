package net.devdome.paperplayer.data.model;

public class Album {

    long id;
    String name;
    String artist;
    String artString;
    boolean fav;
    int numOfSongs;

    public Album(long id, String name, String artist, boolean fav, String artString, int numOfSongs) {
        this.artist = artist;
        this.fav = fav;
        this.id = id;
        this.name = name;
        this.artString = artString;
        this.numOfSongs = numOfSongs;
    }

    public Album(long id, String name, String artist, boolean fav, int numOfSongs) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.fav = fav;
        this.numOfSongs = numOfSongs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtString() {
        return artString;
    }

    public void setArtString(String artString) {
        this.artString = artString;
    }

    public boolean getFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }
}
