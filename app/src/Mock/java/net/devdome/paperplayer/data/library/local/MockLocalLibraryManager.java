package net.devdome.paperplayer.data.library.local;

import android.content.Context;

import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.data.model.Song;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 22-10-2016 12:29 PM
 */

public class MockLocalLibraryManager implements LibraryManager {
    private static final String TAG = "LocalLibraryManager";
    private List<Song> songs = new ArrayList<>();
    private List<Album> albums = new ArrayList<>();

    private Song song1, song2, song3;

    private Album album1, album2, album3;

    public MockLocalLibraryManager(Context context) {
        setDummySongs();
        setDummyAlbums();
    }

    private void setDummySongs() {
        song1 = new Song("I can't let you go", "8701", "Usher", "1998");
        song2 = new Song("Grow", "Revenge of the dreamers II", "Cozz", "2016");
        song3 = new Song("Folgers crystals", "Revenge of the dreamers II", "J. Cole", "2016");

        songs.add(song1);
        songs.add(song2);
        songs.add(song3);
    }

    private void setDummyAlbums() {
        album1 = new Album(1, "8701", "Usher", "https://upload.wikimedia.org/wikipedia/en/6/62/Usher8701Cover.jpg");
        album2 = new Album(2, "Revenge of the dreamers II", "Dreamville", "https://upload.wikimedia.org/wikipedia/en/3/3f/DreamvilleROTD2.jpeg");
        album3 = new Album(3, "Views", "Drake", "https://upload.wikimedia.org/wikipedia/en/6/64/Drakeviewsfromthe6.jpg");

        albums.add(album1);
        albums.add(album2);
        albums.add(album3);
    }

    @Override
    public Observable<List<Song>> fetchAllSongs() {
        return Observable.just(songs);
    }

    @Override
    public Observable<List<Album>> fetchAllAlbums() {
        return Observable.just(albums);
    }

    @Override
    public Observable<Song> getSong(long id) {
        if (id == 1) {
            return Observable.just(song1);
        } else if (id == 2) {
            return Observable.just(song2);
        } else if (id == 3) {
            return Observable.just(song3);
        }
        return Observable.just(null);
    }

}
