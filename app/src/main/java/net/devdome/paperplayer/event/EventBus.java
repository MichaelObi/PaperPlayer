package net.devdome.paperplayer.event;

import android.support.annotation.NonNull;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 12 01 2017 12:38 AM
 */

public interface EventBus {

    void post(@NonNull Object event);

    <T> Observable<T> observable(@NonNull Class<T> eventClass);
}
