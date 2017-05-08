package xyz.michaelobi.paperplayer.data.model;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * PaperPlayer Michael Obi 21 01 2017 8:35 AM
 */

public class Artist {

    private long id;
    private String name;
    private int numOfAlbums;
    private int numOfSongs;
    private String artistKey;

    public Artist(long id, String name, int numOfAlbums, int numOfSongs, String artistKey) {

        this.id = id;
        this.name = name;
        this.numOfAlbums = numOfAlbums;
        this.numOfSongs = numOfSongs;
        this.artistKey = artistKey;
    }

    public static Artist from(Cursor musicCursor) {
        int idColumn = musicCursor.getColumnIndex
                (MediaStore.Audio.Artists._ID);
        int artistColumn = musicCursor.getColumnIndex
                (MediaStore.Audio.Artists.ARTIST);
        int numOfSongsColumn = musicCursor.getColumnIndex
                (MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
        int numOfAlbumsColumn = musicCursor.getColumnIndex
                (MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
        int artistKeyColumn = musicCursor.getColumnIndex
                (MediaStore.Audio.Artists.ARTIST_KEY);

        Artist artist = new Artist(musicCursor.getLong(idColumn),
                musicCursor.getString(artistColumn),
                musicCursor.getInt(numOfAlbumsColumn),
                musicCursor.getInt(numOfSongsColumn),
                musicCursor.getString(artistKeyColumn));
        return artist;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumOfAlbums() {
        return numOfAlbums;
    }

    public void setNumOfAlbums(int numOfAlbums) {
        this.numOfAlbums = numOfAlbums;
    }

    public String getArtistKey() {
        return artistKey;
    }

    public void setArtistKey(String artistKey) {
        this.artistKey = artistKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
