package xyz.michaelobi.paperplayer.data;

import xyz.michaelobi.paperplayer.data.model.Album;
import xyz.michaelobi.paperplayer.data.model.Artist;
import xyz.michaelobi.paperplayer.data.model.Song;

import java.util.List;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 25 10 2016 3:01 AM
 */

public interface MusicRepositoryInterface {

    Observable<List<Song>> getAllSongs();

    Observable<List<Album>> getAllAlbums();

    Observable<Album> getAlbum(long albumId);

    Observable<List<Artist>> getAllArtists();
}
