package net.devdome.paperplayer.presentation.musiclibrary;

import net.devdome.paperplayer.data.MusicLibraryRepoContract;
import net.devdome.paperplayer.data.local.MusicLibraryRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * PaperPlayer
 * Michael Obi
 * 18 10 2016 5:43 AM
 */
@Module
public class LibraryModule {

    @Provides
    @Singleton
    MusicLibraryContract.Presenter provideMusicLibraryPresenter() {
        return new MusicLibraryPresenter();
    }

    @Provides
    @Singleton
    MusicLibraryRepoContract provideMusicLibraryRepository() {
        return new MusicLibraryRepo();
    }
}
