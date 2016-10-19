package net.devdome.paperplayer.data;

import net.devdome.paperplayer.data.model.Song;

import java.util.List;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:22 PM
 */

public interface MusicLibraryRepoContract {
    Observable<List<Song>> fetchSongs(final String searchTerm);
}
