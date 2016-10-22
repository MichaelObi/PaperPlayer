package net.devdome.paperplayer.presentation.musiclibrary.songs;

import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.mvp.Mvp;

import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:39 PM
 */

interface SongsContract {
    interface View extends Mvp.View {
        void showSongsList(List<Song> songs);

        void showLoading();

        void hideLoading();

        void showError(String message);
    }

    interface Presenter extends Mvp.Presenter<View> {
        void getAllSongs();
    }

}
