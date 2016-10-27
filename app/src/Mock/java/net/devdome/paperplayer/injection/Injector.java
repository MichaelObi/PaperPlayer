package net.devdome.paperplayer.injection;

import net.devdome.paperplayer.data.MusicRepository;
import net.devdome.paperplayer.data.MusicRepositoryInterface;
import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.library.local.MockLocalLibraryManager;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 10:36 AM
 */

public class Injector {

    public static MusicRepositoryInterface provideMusicRepository() {
        return new MusicRepository(provideLibraryManager());
    }

    public static LibraryManager provideLibraryManager() {
        return new MockLocalLibraryManager();
    }
}
