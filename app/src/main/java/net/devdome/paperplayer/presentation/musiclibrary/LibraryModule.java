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
class LibraryModule {

    @Provides
    @Singleton
    MusicLibraryPresenter provideMusicLibraryPresenter() {
        return new MusicLibraryPresenter();
    }
}
