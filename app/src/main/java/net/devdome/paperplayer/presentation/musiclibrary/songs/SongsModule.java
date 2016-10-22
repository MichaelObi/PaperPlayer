package net.devdome.paperplayer.presentation.musiclibrary.songs;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * PaperPlayer
 * Michael Obi
 * 18 10 2016 5:43 AM
 */
@Module
class SongsModule {

    @Provides
    @Singleton
    SongsContract.Presenter provideSongsPresenter() {
        return new SongsPresenter();
    }
}
