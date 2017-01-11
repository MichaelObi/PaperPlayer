package net.devdome.paperplayer.data.library;

import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.data.model.Song;

import java.util.List;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:22 PM
 */

public interface LibraryManager {

    /**
     * Fetch all songs from music library
     *
     * @return {@link Observable<List<Song>>}
     */
    Observable<List<Song>> fetchAllSongs();


    /**
     * Fetch all albums from music library
     *
     * @return {@link Observable<List<Album>>}
     */
    Observable<List<Album>> fetchAllAlbums();

    /**
     * Get song with the supplied identifier
     *
     * @return {@link Observable<Song>}
     */
    Observable<Song> getSong(long id);

    /**
     * Fetch all songs from music library
     *
     * @return {@link Observable<List<Song>>}
     */
    Observable<List<Song>> fetchAllAlbumSongs(long albumId);
}
