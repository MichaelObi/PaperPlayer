package net.devdome.paperplayer.presentation.musiclibrary.songs;

import net.devdome.paperplayer.data.MusicLibraryRepoContract;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * PaperPlayer
 * Michael Obi
 * 18 10 2016 5:43 AM
 */
@Module
class SongsModule {

    @Provides
    @Singleton
    SongsContract.Presenter provideSongsPresenter(MusicLibraryRepoContract musicLibraryRepo) {
        return new SongsPresenter(musicLibraryRepo, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
