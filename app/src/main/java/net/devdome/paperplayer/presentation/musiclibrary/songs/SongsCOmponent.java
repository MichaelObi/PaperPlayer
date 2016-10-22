package net.devdome.paperplayer.presentation.musiclibrary.songs;

import net.devdome.paperplayer.presentation.musiclibrary.LibraryModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * PaperPlayer
 * Michael Obi
 * 18 10 2016 5:47 AM
 */

@Singleton
@Component(modules = {SongsModule.class, LibraryModule.class})
interface SongsComponent {

    SongsContract.Presenter provideSongPresenter();

    void inject(SongsFragment songsFragment);
}
