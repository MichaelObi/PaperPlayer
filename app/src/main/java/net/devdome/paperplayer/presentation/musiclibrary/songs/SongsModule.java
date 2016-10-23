package net.devdome.paperplayer.presentation.musiclibrary.songs;

import net.devdome.paperplayer.data.library.LibraryManager;

import javax.inject.Inject;
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
public class SongsModule {

    @Provides
    @Singleton
    SongsContract.Presenter provideSongsPresenter() {
        return new SongsPresenter(Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
