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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface;
import xyz.michaelobi.paperplayer.data.library.LibraryManager;
import xyz.michaelobi.paperplayer.data.library.local.MockLocalLibraryManager;
import xyz.michaelobi.paperplayer.data.model.Song;
import xyz.michaelobi.paperplayer.injection.Injector;
import xyz.michaelobi.paperplayer.mvp.ListViewContract;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * PaperPlayer
 * Michael Obi
 * 22 10 2016 2:50 AM
 */
public class SongsPresenterTest {

    @Mock
    MusicRepositoryInterface musicRepository;

    @Mock
    ListViewContract.View view;

    private SongsPresenter albumsPresenter;

    private LibraryManager libraryManager;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        libraryManager = Injector.provideLibraryManager(null);
        albumsPresenter = new SongsPresenter(musicRepository, Schedulers.immediate(), Schedulers.immediate());
        albumsPresenter.attachView(view);
    }

    @Test
    public void getAllSongs_returnSongList() throws Exception {
        when(musicRepository.getAllSongs()).thenReturn(libraryManager.fetchAllSongs());
        albumsPresenter.getAll();
        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).showError(anyString());
        verify(view).showList(MockLocalLibraryManager.songs);

    }

}