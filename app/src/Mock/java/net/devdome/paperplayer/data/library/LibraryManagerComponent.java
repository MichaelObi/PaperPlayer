package net.devdome.paperplayer.data.library;

import net.devdome.paperplayer.data.library.LibraryManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * PaperPlayer
 * Michael Obi
 * 22 10 2016 4:18 PM
 */
@Singleton
@Component(modules = {LibraryManagerModule.class})
interface LibraryManagerComponent {

    LibraryManager provideLibraryManager();

    void inject(LibraryManager musicLibraryActivity);
}
