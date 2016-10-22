package net.devdome.paperplayer.presentation.musiclibrary.songs;

import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:45 PM
 */

class SongsPresenter extends BasePresenter<SongsContract.View> implements
        SongsContract.Presenter {

    @Override
    public List<Song> getAllSongs() {
        return new ArrayList<>();
    }
}
