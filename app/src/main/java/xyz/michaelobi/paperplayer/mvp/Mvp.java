package xyz.michaelobi.paperplayer.mvp;

/**
 * Mvp
 * Michael Obi
 * 15 10 2016 2:52 PM
 */

public interface Mvp {

    public interface View {
    }

    public interface Presenter<V extends View> {
        void attachView(V view);

        void detachView();
    }
}
