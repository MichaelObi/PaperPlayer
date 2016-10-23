package net.devdome.paperplayer.presentation.musiclibrary.songs;

import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.library.LibraryManagerModule;
import net.devdome.paperplayer.presentation.musiclibrary.MusicLibraryModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * PaperPlayer
 * Michael Obi
 * 18 10 2016 5:47 AM
 */

@Singleton
@Component(modules = {MusicLibraryModule.class, LibraryManagerModule.class, SongsModule.class})
interface SongsComponent {

    SongsContract.Presenter provideSongPresenter();

    LibraryManager provideLibraryManager();

    void inject(SongsFragment songsFragment);
}
