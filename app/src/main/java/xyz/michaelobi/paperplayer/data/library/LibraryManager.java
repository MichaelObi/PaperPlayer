package xyz.michaelobi.paperplayer.data.library;


import java.util.List;

import rx.Observable;
import xyz.michaelobi.paperplayer.data.model.Album;
import xyz.michaelobi.paperplayer.data.model.Artist;
import xyz.michaelobi.paperplayer.data.model.Song;

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
