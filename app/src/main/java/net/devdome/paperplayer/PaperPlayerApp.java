package net.devdome.paperplayer;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import net.devdome.paperplayer.event.EventBus;
import net.devdome.paperplayer.injection.Injector;
import net.devdome.paperplayer.playback.PlaybackService;
import net.devdome.paperplayer.playback.events.PlaybackStart;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 11:00 AM
 */

public class PaperPlayerApp extends Application {
    private EventBus bus;

    public PaperPlayerApp() {
        super();
        bus = Injector.provideEventBus();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bus.observable(PlaybackStart.class).subscribe(event -> {
            Toast.makeText(this, "Playback started", Toast.LENGTH_SHORT).show();
        });

        startService(new Intent(this, PlaybackService.class));
    }
}

