package net.devdome.paperplayer.presentation.album;

import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.mvp.Mvp;

import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 11 01 2017 7:43 AM
 */

public class AlbumContract {
    interface View extends Mvp.View {
        void initialize();

        void showSongs(List<Song> songs);

        void showAlbumArt();

        void showAlbumMetadata();

        void showLoading();

        void hideLoading();

        void showError(String message);
    }

    interface Presenter extends Mvp.Presenter<AlbumContract.View> {
        void getSongs(long albumId);

        void getAlbum(long albumId);
    }
}
