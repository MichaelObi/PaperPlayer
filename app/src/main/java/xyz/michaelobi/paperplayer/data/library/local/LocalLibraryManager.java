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

package xyz.michaelobi.paperplayer.data.library.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import xyz.michaelobi.paperplayer.data.library.LibraryManager;
import xyz.michaelobi.paperplayer.data.model.Album;
import xyz.michaelobi.paperplayer.data.model.Artist;
import xyz.michaelobi.paperplayer.data.model.Song;

/**
 * PaperPlayer Michael Obi 22 10 2016 12:29 PM
 */

public class LocalLibraryManager implements LibraryManager {
    private final Context context;

    public LocalLibraryManager(Context context) {
        this.context = context;
    }


    private static Observable<Cursor> create(Cursor cursor) {
        return Observable.create(sub -> {
            if (sub.isUnsubscribed()) return;

            try {
                while (cursor.moveToNext()) {
                    if (sub.isUnsubscribed()) return;
                    sub.onNext(cursor);
                }
                sub.onCompleted();
            } catch (Exception e) {
                sub.onError(e);
            }
        });
    }

    @Override
    public Observable<List<Song>> fetchAllSongs() {
        QueryBuilder builder = new QueryBuilder(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                .where(MediaStore.Audio.Media.IS_MUSIC + "=1")
                .orderBy(MediaStore.Audio.Media.TITLE);
        Cursor cursor = builder.query();

        return create(cursor)
                .map(Song.Companion::from)
                .doOnCompleted(cursor::close)
                .toList();
    }

    @Override
    public Observable<List<Song>> fetchSongsForAlbum(long id) {
        QueryBuilder builder = new QueryBuilder(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                .where(String.format(Locale.getDefault(), "%s=%d", MediaStore.Audio.Media.ALBUM_ID, id))
                .orderBy(MediaStore.Audio.Media.TITLE);
        Cursor cursor = builder.query();

        return create(cursor)
                .map(Song.Companion::from)
                .doOnCompleted(cursor::close)
                .toList();
    }

    @Override
    public Observable<List<Album>> fetchAllAlbums() {
        QueryBuilder builder = new QueryBuilder(context, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI)
                .orderBy(MediaStore.Audio.Albums.ALBUM);
        Cursor cursor = builder.query();
        return create(cursor)
                .map(Album.Companion::from)
                .doOnCompleted(cursor::close)
                .toList();
    }

    @Override
    public Observable<Song> getSong(long id) {
        return null;
    }

    @Override
    public Observable<Album> getAlbum(long id) {
        QueryBuilder builder = new QueryBuilder(context, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI)
                .where(String.format(Locale.getDefault(), "%s=%d", MediaStore.Audio.Albums._ID, id))
                .orderBy(MediaStore.Audio.Albums.ALBUM);
        Cursor cursor = builder.query();
        return create(cursor)
                .take(1)
                .map(Album.Companion::from)
                .doOnCompleted(cursor::close)
                .first();
    }

    @Override
    public Observable<List<Artist>> fetchAllArtists() {
        QueryBuilder builder = new QueryBuilder(context, MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI)
                .orderBy(MediaStore.Audio.Artists.ARTIST);
        Cursor cursor = builder.query();
        return create(cursor)
                .map(Artist.Companion::from)
                .doOnCompleted(cursor::close)
                .toList();
    }

}
