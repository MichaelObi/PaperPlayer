package net.devdome.paperplayer.presentation.musiclibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:36 PM
 */

public class LibraryActivity extends AppCompatActivity implements MusicLibraryContract.View {

    private MusicLibraryContract.Presenter presenter;

//    private LibraryComponent component;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerLibraryComponent.builder()
                .build()
                .inject(this);
    }
}
