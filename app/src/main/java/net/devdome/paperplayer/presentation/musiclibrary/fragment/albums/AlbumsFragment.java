package net.devdome.paperplayer.presentation.musiclibrary.fragment.albums;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.injection.Injector;
import net.devdome.paperplayer.presentation.musiclibrary.fragment.FragmentsContract;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 4:00 PM
 */

public class AlbumsFragment extends Fragment implements FragmentsContract.View<Album> {

    FragmentsContract.Presenter songsPresenter;
    private AlbumsAdapter albumsAdapter;
    private Context context;
    private RecyclerView recyclerViewSongs;
    private ProgressBar progressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songsPresenter = new AlbumsPresenter(Injector.provideLibraryManager(), Schedulers.io(),
                AndroidSchedulers.mainThread());
        songsPresenter.attachView(this);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_albums, container, false);
        recyclerViewSongs = (RecyclerView) v.findViewById(R.id.rv_albums_grid);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar_loading);

        recyclerViewSongs.setLayoutManager(new GridLayoutManager(context, 2));
        albumsAdapter = new AlbumsAdapter(null, context);
        recyclerViewSongs.setAdapter(albumsAdapter);
        songsPresenter.getAll();
        return v;
    }


    @Override
    public void showList(List<Album> albums) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {

    }
}
