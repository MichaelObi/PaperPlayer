package net.devdome.paperplayer.data.library.local;

import net.devdome.paperplayer.data.library.LibraryManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * PaperPlayer
 * Michael Obi
 * 22 10 2016 4:18 PM
 */
@Module
public class LibraryManagerModule {

    @Provides
    @Singleton
    LibraryManager provideLibraryManager() {
        return new LocalLibraryManager();
    }
}
