package net.devdome.paperplayer.presentation.musiclibrary.fragment;

import net.devdome.paperplayer.mvp.Mvp;

import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:39 PM
 */

public interface LibraryFragmentsContract {
    interface View<T> extends Mvp.View {
        void showList(List<T> items);

        void showLoading();

        void hideLoading();

        void showError(String message);
    }

    interface Presenter extends Mvp.Presenter<View> {
        void getAll();
    }

}
