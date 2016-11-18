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
import android.widget.Toast;

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

    FragmentsContract.Presenter presenter;
    private AlbumsAdapter albumsAdapter;
    private Context context;
    private RecyclerView recyclerViewAlbums;
    private ProgressBar progressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new AlbumsPresenter(Injector.provideMusicRepository(getContext()), Schedulers.io(),
                AndroidSchedulers.mainThread());
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_albums, container, false);
        recyclerViewAlbums = (RecyclerView) v.findViewById(R.id.rv_albums_grid);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar_loading);
        presenter.attachView(this);

        recyclerViewAlbums.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerViewAlbums.setHasFixedSize(true);
        albumsAdapter = new AlbumsAdapter(null, context);
        recyclerViewAlbums.setAdapter(albumsAdapter);
        presenter.getAll();
        return v;
    }


    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void showList(List<Album> albums) {
        recyclerViewAlbums.setVisibility(View.VISIBLE);
        albumsAdapter.setAlbums(albums);
    }

    @Override
    public void showLoading() {
        recyclerViewAlbums.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
