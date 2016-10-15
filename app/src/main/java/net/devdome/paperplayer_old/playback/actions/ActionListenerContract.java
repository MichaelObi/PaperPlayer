package net.devdome.paperplayer_old.playback.actions;

import android.content.Context;
import android.content.Intent;

public interface ActionListenerContract {
    void execute(Intent intent, Context context);
}
