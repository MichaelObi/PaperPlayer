package net.devdome.paperplayer.injection;

import android.content.Context;

import net.devdome.paperplayer.data.MusicRepository;
import net.devdome.paperplayer.data.MusicRepositoryInterface;
import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.library.local.LocalLibraryManager;
import net.devdome.paperplayer.event.EventBus;
import net.devdome.paperplayer.event.RxBus;
import net.devdome.paperplayer.playback.queue.LocalQueueManager;
import net.devdome.paperplayer.playback.queue.QueueManager;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 10:36 AM
 */

public class Injector {

    private static EventBus eventBus;

    public static EventBus provideEventBus() {
        if (eventBus == null) {
            eventBus = new RxBus();
        }

        return eventBus;
    }

    public static QueueManager provideQueueManager() {
        return new LocalQueueManager();
    }

    public static MusicRepositoryInterface provideMusicRepository(Context context) {
        return new MusicRepository(provideLibraryManager(context));
    }

    public static LibraryManager provideLibraryManager(Context context) {
        return new LocalLibraryManager(context);
    }
}
