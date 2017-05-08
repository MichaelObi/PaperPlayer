package xyz.michaelobi.paperplayer.data;

import xyz.michaelobi.paperplayer.data.library.LibraryManager;
import xyz.michaelobi.paperplayer.data.model.Album;
import xyz.michaelobi.paperplayer.data.model.Artist;
import xyz.michaelobi.paperplayer.data.model.Song;

import java.io.IOException;
import java.util.List;

import rx.Observable;

/**
 * PaperPlayer Michael Obi 25 10 2016 3:03 AM
 */
public class MusicRepository implements MusicRepositoryInterface {


    private LibraryManager libraryManager;

    public MusicRepository(LibraryManager libraryManager) {

        this.libraryManager = libraryManager;
    }

    @Override
    public Observable<List<Song>> getAllSongs() {
        return Observable.defer(() -> libraryManager.fetchAllSongs())
                .retryWhen(observable -> observable.flatMap(o -> {
                            if (o instanceof IOException) {
                                return null;
                            }
                            return Observable.error(o);
                        })

                );
    }

    @Override
    public Observable<List<Album>> getAllAlbums() {
        return Observable.defer(() -> libraryManager.fetchAllAlbums())
                .retryWhen(observable -> observable.flatMap(o -> {
                            if (o instanceof IOException) {
                                return null;
                            }
                            return Observable.error(o);
                        })
                );
    }

    @Override
    public Observable<Album> getAlbum(long albumId) {
        return Observable.defer(() -> libraryManager.getAlbum(albumId))
                .retryWhen(observable -> observable.flatMap(o -> {
                            if (o instanceof IOException) {
                                return null;
                            }
                            return Observable.error(o);
                        })
                );
    }

    @Override
    public Observable<List<Artist>> getAllArtists() {
        return Observable.defer(() -> libraryManager.fetchAllArtists())
                .retryWhen(observable -> observable.flatMap(o -> {
                            if (o instanceof IOException) {
                                return null;
                            }
                            return Observable.error(o);
                        })
                );
    }
}
