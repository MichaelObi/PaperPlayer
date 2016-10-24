package net.devdome.paperplayer.injection;

import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.library.local.MockLocalLibraryManager;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 10:36 AM
 */

public class Injector {
    public static LibraryManager provideLibraryManager() {
        return new MockLocalLibraryManager();
    }
}
