package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs;

import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface;
import xyz.michaelobi.paperplayer.data.model.Song;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.LibraryFragmentsContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface;
import xyz.michaelobi.paperplayer.data.model.Song;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.LibraryFragmentsContract;

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
    LibraryFragmentsContract.View view;

    private SongsPresenter albumsPresenter;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        albumsPresenter = new SongsPresenter(musicRepository, Schedulers.immediate(), Schedulers.immediate());
        albumsPresenter.attachView(view);
    }

    @Test
    public void getAllSongs_returnSongList() throws Exception {
        List<Song> songList = getDummySongList();
        when(musicRepository.getAllSongs()).thenReturn(Observable.just(songList));

        albumsPresenter.getAll();
        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).showError(anyString());
        verify(view).showList(songList);

    }

    public List<Song> getDummySongList() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song(1, "I can't let you go", "8701", "Usher", "1998"));
        songs.add(new Song(2, "Grow", "Revenge of the dreamers II", "Cozz", "2016"));
        songs.add(new Song(3, "Folgers crystals", "Revenge of the dreamers II", "J. Cole", "2016"));

        return songs;
    }

}