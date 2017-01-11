package net.devdome.paperplayer.presentation.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.devdome.paperplayer.data.model.Song;

import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 11 01 2017 7:47 AM
 */

public class AlbumActivity extends AppCompatActivity implements AlbumContract.View {

    AlbumContract.Presenter albumPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumPresenter = new AlbumPresenter();
        albumPresenter.attachView(this);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void showSongs(List<Song> songs) {

    }

    @Override
    public void showAlbumArt() {

    }

    @Override
    public void showAlbumMetadata() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {

    }
}
