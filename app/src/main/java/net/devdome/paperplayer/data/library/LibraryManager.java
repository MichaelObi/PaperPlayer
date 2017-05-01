package net.devdome.paperplayer.data.library;


import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.data.model.Artist;
import net.devdome.paperplayer.data.model.Song;

import java.util.List;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:22 PM
 */

public interface LibraryManager {
    Observable<List<Song>> fetchAllSongs();

    Observable<List<Album>> fetchAllAlbums();

    Observable<Song> getSong(long id);

    Observable<Album> getAlbum(long id);

    Observable<List<Artist>> fetchAllArtists();
}
