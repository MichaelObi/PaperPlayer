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

package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs;

import xyz.michaelobi.paperplayer.data.MusicRepository;
import xyz.michaelobi.paperplayer.data.model.Album;
import xyz.michaelobi.paperplayer.mvp.ListViewContract;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.albums.AlbumsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * PaperPlayer Michael Obi 22 10 2016 2:50 AM
 */
public class AlbumsPresenterTest {

    @Mock
    MusicRepository musicRepository;

    @Mock
    ListViewContract.View view;

    private AlbumsPresenter albumsPresenter;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        albumsPresenter = new AlbumsPresenter(musicRepository, Schedulers.immediate(), Schedulers.immediate());
        albumsPresenter.attachView(view);
    }

    @Test
    public void getAllAlbums_returnAlbumList() throws Exception {
        List<Album> albums = getDummyAlbumList();
        when(musicRepository.getAllAlbums()).thenReturn(Observable.just(albums));

        albumsPresenter.getAll();
        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).showError(anyString());
        verify(view).showList(albums);
    }

    private List<Album> getDummyAlbumList() {
        ArrayList<Album> albums = new ArrayList<>();
        albums.add(new Album(1, "8701", "Usher", false,null, 15));
        albums.add(new Album(2, "Revenge of the Dreamers II", "Dreamville", false, null, 20));

        return albums;
    }

}