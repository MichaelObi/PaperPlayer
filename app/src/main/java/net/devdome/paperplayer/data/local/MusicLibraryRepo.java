package net.devdome.paperplayer.data.local;

import net.devdome.paperplayer.data.MusicLibraryRepoContract;
import net.devdome.paperplayer.data.model.Song;

import java.util.List;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:35 PM
 */

public class MusicLibraryRepo implements MusicLibraryRepoContract {

    @Override
    public Observable<List<Song>> fetchAllSongs() {
        return null;
    }
}
