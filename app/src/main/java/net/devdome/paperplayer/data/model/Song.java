package net.devdome.paperplayer.data.model;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:23 PM
 */

public class Song {

    private String title, album, artist, year;
    private long id = 5;
    private long albumId;

    public Song() {
    }

    public Song(String title, String album, String artist, long albumId,String year) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.year = year;
    }

    public static Song from(Cursor cursor) {
        int titleColumn = cursor.getColumnIndex
                (android.provider.MediaStore.Audio.Media.TITLE);
        int idColumn = cursor.getColumnIndex
                (android.provider.MediaStore.Audio.Media._ID);
        int artistColumn = cursor.getColumnIndex
                (android.provider.MediaStore.Audio.Media.ARTIST);
        int pathColumn = cursor.getColumnIndex
                (MediaStore.Audio.Media.DATA);
        int albumIdColumn = cursor.getColumnIndex
                (MediaStore.Audio.Media.ALBUM_ID);
        int albumColumn = cursor.getColumnIndex
                (MediaStore.Audio.Media.ALBUM);

        return new Song(
                cursor.getString(titleColumn),
                cursor.getString(albumColumn),
                cursor.getString(artistColumn),
                cursor.getLong(albumIdColumn),
                ""
        );
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

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
}
