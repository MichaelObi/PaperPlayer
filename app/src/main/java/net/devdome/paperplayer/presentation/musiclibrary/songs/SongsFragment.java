package net.devdome.paperplayer.presentation.musiclibrary.songs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Song;

import java.util.List;

import javax.inject.Inject;

/**
 * PaperPlayer
 * Michael Obi
 * 19 10 2016 6:15 PM
 */

public class SongsFragment extends Fragment implements SongsContract.View {

    @Inject
    SongsContract.Presenter songsPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSongsComponent.builder()
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_songs, container, false);
        songsPresenter.attachView(this);
        songsPresenter.getAllSongs();
        return v;
    }

    @Override
    public void showSongsList(List<Song> songs) {

    }

    @Override
    public void onDestroyView() {
        songsPresenter.detachView();
        super.onDestroyView();
    }
}
