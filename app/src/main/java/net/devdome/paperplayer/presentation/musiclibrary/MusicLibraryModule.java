package net.devdome.paperplayer.presentation.musiclibrary;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * PaperPlayer
 * Michael Obi
 * 18 10 2016 5:43 AM
 */
@Module
public class MusicLibraryModule {

    @Provides
    @Singleton
    MusicLibraryContract.Presenter provideMusicLibraryPresenter() {
        return new MusicLibraryPresenter();
    }

}
