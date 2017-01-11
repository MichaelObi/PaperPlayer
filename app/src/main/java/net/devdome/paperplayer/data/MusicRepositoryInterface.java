package net.devdome.paperplayer.data;

import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.data.model.Song;

import java.util.List;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 25 10 2016 3:01 AM
 */

public interface MusicRepositoryInterface {

    public Observable<List<Song>> getAllSongs();

    public Observable<List<Album>> getAllAlbums();

    public Observable<List<Song>> getSongsFromAlbum(long albumId);
}
