package net.devdome.paperplayer.presentation.musiclibrary;

import net.devdome.paperplayer.data.library.LibraryManagerModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * PaperPlayer
 * Michael Obi
 * 18 10 2016 5:47 AM
 */

@Singleton
@Component(modules = {MusicLibraryModule.class, LibraryManagerModule.class})
interface MusicLibraryComponent {

    MusicLibraryContract.Presenter provideMusicLibraryPresenter();

    void inject(MusicLibraryActivity musicLibraryActivity);
}
