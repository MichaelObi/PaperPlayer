package net.devdome.paperplayer.presentation.musiclibrary;

import javax.inject.Singleton;

import dagger.Component;

/**
 * PaperPlayer
 * Michael Obi
 * 18 10 2016 5:47 AM
 */

@Singleton
@Component(modules = {LibraryModule.class})
interface LibraryComponent {

    MusicLibraryContract.Presenter provideMusicLibraryPresenter();

    void inject(LibraryActivity libraryActivity);
}
