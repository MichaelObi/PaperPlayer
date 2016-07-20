package net.devdome.paperplayer.playback.actions;

import android.content.Context;
import android.content.Intent;

public interface ActionListenerContract {
    void execute(Intent intent, Context context);
}
