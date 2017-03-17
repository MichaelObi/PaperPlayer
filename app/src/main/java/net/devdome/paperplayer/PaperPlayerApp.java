package net.devdome.paperplayer;

import android.app.Application;
import android.content.Intent;

import net.devdome.paperplayer.event.EventBus;
import net.devdome.paperplayer.injection.Injector;
import net.devdome.paperplayer.playback.PlaybackService;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 11:00 AM
 */

public class PaperPlayerApp extends Application {
    public PaperPlayerApp() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, PlaybackService.class));
    }
}

