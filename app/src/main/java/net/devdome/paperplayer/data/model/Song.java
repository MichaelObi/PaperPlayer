package net.devdome.paperplayer.data.model;

import android.graphics.Bitmap;


import java.util.ArrayList;

public class Song {

    long id;
    long albumId;
    String name;
    String artist;
    String path;
    String albumName;
    String mood;
    Boolean fav;
    Bitmap art;
    int count;

    public Song(long id, String name, String artist, String path,
                Boolean fav, long albumId, String albumName, int count, String mood) {

        this.id = id;
        this.name = name;
        this.artist = artist;
        this.path = path;
        this.fav = fav;
        this.albumId = albumId;
        this.albumName = albumName;
        this.count = count;
        this.mood = mood;
    }

//    public static Song fromRealmObject(PlaylistItem playlistItem) {
//        return new Song(playlistItem.getSongRealId(),
//                playlistItem.getSongName(), playlistItem.getSongArtist(),
//                playlistItem.getSongPath(), playlistItem.getSongFav(),
//                playlistItem.getSongAlbumId(), playlistItem.getSongAlbumName(), playlistItem.getSongCount(),
//                playlistItem.getSongMood());
//    }


    private static void returnSongs(ArrayList<Song> songList) {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public Boolean getFav() {
        return fav;
    }

    public void setFav(Boolean fav) {
        this.fav = fav;
    }

    public Bitmap getArt() {
        return art;
    }

    public void setArt(Bitmap art) {
        this.art = art;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
