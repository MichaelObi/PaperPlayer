package net.devdome.paperplayer.presentation.musiclibrary.songs;

import net.devdome.paperplayer.data.MusicLibraryRepoContract;
import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.data.model.SongList;

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
public class SongsPresenterTest {

    @Mock
    MusicLibraryRepoContract musicLibraryRepo;

    @Mock
    SongsContract.View view;

    private SongsPresenter songsPresenter;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        songsPresenter = new SongsPresenter(musicLibraryRepo, Schedulers.immediate(), Schedulers.immediate());
        songsPresenter.attachView(view);
    }

    @Test
    public void getAllSongs_returnSongList() throws Exception {
        SongList songList = getDummySongList();
        when(musicLibraryRepo.fetchAllSongs()).thenReturn(Observable.<List<Song>>just(songList.getSongs()));

        songsPresenter.getAllSongs();
        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).showError(anyString());
        verify(view).showSongsList(songList.getSongs());

    }

    public SongList getDummySongList() {
        ArrayList<Song> songs = new ArrayList<>();
        songs.add(new Song());
        songs.add(new Song());

        return new SongList(songs);
    }

}