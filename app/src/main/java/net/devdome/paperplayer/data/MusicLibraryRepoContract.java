package net.devdome.paperplayer.data;

import java.util.List;

import rx.Observable;
import net.devdome.paperplayer.data.local.model.Song;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:22 PM
 */

public interface MusicLibraryRepoContract {
    Observable<List<Song>> fetchSongs(final String searchTerm);
}
