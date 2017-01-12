package net.devdome.paperplayer.playback;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.event.EventBus;
import net.devdome.paperplayer.injection.Injector;
import net.devdome.paperplayer.playback.events.PlaybackStart;

/**
 * PaperPlayer
 * Michael Obi
 * 11 01 2017 10:46 PM
 */
public class PlaybackService extends Service {
    private EventBus eventBus;

    public PlaybackService() {
        eventBus = Injector.provideEventBus();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        eventBus.post(new PlaybackStart(new Song()));
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}