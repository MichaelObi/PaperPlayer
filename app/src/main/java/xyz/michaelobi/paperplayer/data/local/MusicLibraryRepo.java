package xyz.michaelobi.paperplayer.data.local;

import java.util.List;

import rx.Observable;
import xyz.michaelobi.paperplayer.data.MusicLibraryRepoContract;
import xyz.michaelobi.paperplayer.data.local.model.Song;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:35 PM
 */

public class MusicLibraryRepo implements MusicLibraryRepoContract {
    @Override
    public Observable<List<Song>> fetchSongs(String searchTerm) {
        return null;
    }
}
