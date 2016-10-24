package net.devdome.paperplayer.data.library.local;

import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.data.model.Song;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 22 10 2016 12:29 PM
 */

public class MockLocalLibraryManager implements LibraryManager {
    private static final String TAG = "LocalLibraryManager";
    private List<Song> songs = new ArrayList<>();

    private Song song1, song2, song3;

    public MockLocalLibraryManager() {
        song1 = new Song("I can't let you go", "8701", "Usher", "1998");
        song2 = new Song("Grow", "Revenge of the dreamers II", "Cozz", "2016");
        song3 = new Song("Folgers crystals", "Revenge of the dreamers II", "J. Cole", "2016");

        songs.add(song1);
        songs.add(song2);
        songs.add(song3);

    }

    @Override
    public Observable<List<Song>> fetchAllSongs() {
        return Observable.just(songs);
    }

    @Override
    public Observable<List<Album>> fetchAllAlbums() {
        return null;
    }

}
