package net.devdome.paperplayer.presentation.musiclibrary.fragment.songs;

import net.devdome.paperplayer.data.MusicRepository;
import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.presentation.musiclibrary.fragment.FragmentsContract;
import net.devdome.paperplayer.presentation.musiclibrary.fragment.albums.AlbumsPresenter;

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
 * PaperPlayer
 * Michael Obi
 * 22 10 2016 2:50 AM
 */
public class AlbumsPresenterTest {

    @Mock
    MusicRepository musicRepository;

    @Mock
    FragmentsContract.View view;

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
        albums.add(new Album(1, "8701", "Usher", false, null, 20));
        albums.add(new Album(2, "Revenge of the Dreamers II", "Dreamville", false, null, 20));

        return albums;
    }

}