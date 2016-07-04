package net.devdome.paperplayer.playback;

import android.content.Context;
import android.content.Intent;

interface ActionListenerContract {
    void execute(Intent intent, Context context);
}
