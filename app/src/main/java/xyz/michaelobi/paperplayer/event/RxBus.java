package xyz.michaelobi.paperplayer.event;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * PaperPlayer Michael Obi 12 01 2017 12:06 AM
 */

public class RxBus implements EventBus {

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public RxBus() {
    }

    @Override
    public void post(@NonNull Object event) {
        if (this.bus.hasObservers()) {
            this.bus.onNext(event);
        }
    }

    @Override
    public <T> Observable<T> observable(@NonNull Class<T> eventClass) {
        return this.bus
                .filter(o -> o != null) // Filter out null objects, better safe than sorry
                .filter(eventClass::isInstance) // We're only interested in a specific event class
                .cast(eventClass); // Cast it for easier usage
    }

    @Override
    public void cleanup() {

    }
}
