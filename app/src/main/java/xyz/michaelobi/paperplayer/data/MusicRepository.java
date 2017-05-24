/*
 * MIT License
 *
 * Copyright (c) 2017 MIchael Obi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
