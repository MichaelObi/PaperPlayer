package net.devdome.paperplayer_old.data.model;

public class Artist {
    private long id;
    private String name;
    private int numOfAlbums;
    private int numOfSongs;
    private String artistKey;

    public Artist(long id, String name, String artistKey) {
        this.id = id;
        this.name = name;
        this.artistKey = artistKey;
    }

    public Artist(long id, String name, int numOfAlbums, int numOfSongs) {
        this.id = id;
        this.name = name;
        this.numOfAlbums = numOfAlbums;
        this.numOfSongs = numOfSongs;
    }

    public Artist(long id, String name, int numOfAlbums, int numOfSongs, String artistKey) {
        this.id = id;
        this.name = name;
        this.numOfAlbums = numOfAlbums;
        this.numOfSongs = numOfSongs;
        this.artistKey = artistKey;
    }


    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getNumOfAlbums() {
        return numOfAlbums;
    }

    public void setNumOfAlbums(int numOfAlbums) {
        this.numOfAlbums = numOfAlbums;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
