package net.devdome.paperplayer.data;

import junit.framework.Assert;

import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.data.model.Song;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * PaperPlayer Michael Obi 25 10 2016 3:50 AM
 */
public class MusicRepositoryTest {

    private final String albumName = "Revenge";
    private final String albumArtist = "Dreamville";
    private final String albumArtPath = "file://hello.jpg";
    private final String songTitle = "Folgers Crystals";

    @Mock
    LibraryManager libraryManager;
    private MusicRepositoryInterface musicRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        musicRepository = new MusicRepository(libraryManager);

    }

    @Test
    public void getAllSongs_listsSongs_makesCorrectLibraryCalls() {
        when(libraryManager.fetchAllSongs()).thenReturn(Observable.just(songList()));

        TestSubscriber<List<Song>> subscriber = new TestSubscriber<>();
        musicRepository.getAllSongs().subscribe(subscriber);

        List<Song> songs = subscriber.getOnNextEvents().get(0);
        Assert.assertEquals(songTitle, songs.get(0).getTitle());
        Assert.assertEquals(albumArtist, songs.get(0).getArtist());
        Assert.assertEquals(albumName, songs.get(0).getAlbum());

        verify(libraryManager).fetchAllSongs();

    }

    @Test
    public void getAllAlbums_listsAlbums_returnsCorrectData() {
        when(libraryManager.fetchAllAlbums()).thenReturn(Observable.just(albumList()));

        TestSubscriber<List<Album>> subscriber = new TestSubscriber<>();
        musicRepository.getAllAlbums().subscribe(subscriber);

        List<Album> albums = subscriber.getOnNextEvents().get(0);
        Assert.assertEquals(albumName, albums.get(0).getTitle());

        verify(libraryManager).fetchAllAlbums();

    }

    private List<Song> songList() {
        Song song = new Song(1, songTitle, albumName, albumArtist, "2016", "");

        List<Song> songs = new ArrayList<>();
        songs.add(song);

        return songs;
    }

    private List<Album> albumList() {

        Album song = new Album(1, albumName, albumArtist, false, albumArtPath, 3);

        List<Album> albums = new ArrayList<>();
        albums.add(song);

        return albums;
    }

}