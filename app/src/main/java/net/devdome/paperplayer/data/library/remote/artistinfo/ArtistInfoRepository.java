package net.devdome.paperplayer.data.library.remote.artistinfo;

import net.devdome.paperplayer.data.library.remote.artistinfo.model.spotify.Artist;

import java.util.List;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 19 03 2017 8:27 AM
 */

public interface ArtistInfoRepository {
    // TODO: Implement this, write tests
    public Observable<List<Artist>> artistSearch(String artistName);
}
