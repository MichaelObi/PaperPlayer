package net.devdome.paperplayer.injection;

import android.content.Context;

import net.devdome.paperplayer.data.MusicRepository;
import net.devdome.paperplayer.data.MusicRepositoryInterface;
import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.library.local.LocalLibraryManager;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 10:36 AM
 */

public class Injector {
    public static MusicRepositoryInterface provideMusicRepository(Context context) {
        return new MusicRepository(provideLibraryManager(context));
    }

    public static LibraryManager provideLibraryManager(Context context) {
        return new LocalLibraryManager(context);
    }
}
