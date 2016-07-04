package net.devdome.paperplayer;

import android.app.Application;
import android.content.Intent;

import net.devdome.paperplayer.playback.PlayerService;

public class PaperPlayer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, PlayerService.class));
    }
}
