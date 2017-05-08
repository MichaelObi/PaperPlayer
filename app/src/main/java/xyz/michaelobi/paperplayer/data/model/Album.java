package xyz.michaelobi.paperplayer.data.model;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 4:06 PM
 */

public class Album {

    private static final String TAG = "Album";
    private boolean favorite;
    private int numberOfSongs;
    private long id;
    private String title;
    private String artist;
    private String artPath;

    public Album(long id, String title, String artist, boolean favorite, String artPath, int
            numberOfSongs) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.favorite = favorite;
        this.artPath = artPath;
        this.numberOfSongs = numberOfSongs;
    }

    public static Album from(Cursor cursor) {
        int titleColumn = cursor.getColumnIndex
                (MediaStore.Audio.Albums.ALBUM);
        int idColumn = cursor.getColumnIndex
                (android.provider.MediaStore.Audio.Albums._ID);
        int artistColumn = cursor.getColumnIndex
                (android.provider.MediaStore.Audio.Albums.ARTIST);
        int numOfSongsColumn = cursor.getColumnIndex
                (android.provider.MediaStore.Audio.Albums.NUMBER_OF_SONGS);
        int albumArtColumn = cursor.getColumnIndex
                (android.provider.MediaStore.Audio.Albums.ALBUM_ART);

        return new Album(cursor.getLong(idColumn),
                cursor.getString(titleColumn),
                cursor.getString(artistColumn),
                false, cursor.getString(albumArtColumn),
                cursor.getInt(numOfSongsColumn));

    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
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

    @Override
    public String toString() {
        return "Album{" +
                "favorite=" + favorite +
                ", numberOfSongs=" + numberOfSongs +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", artPath='" + artPath + '\'' +
                '}';
    }

    public File getArt() {
        try {
            return new File(artPath);
        } catch (NullPointerException ex) {
            Log.d(TAG, ex.getMessage());
            return null;
        }
    }
}
