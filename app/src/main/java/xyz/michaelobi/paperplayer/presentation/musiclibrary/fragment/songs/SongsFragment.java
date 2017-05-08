package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import xyz.michaelobi.paperplayer.R;
import xyz.michaelobi.paperplayer.data.model.Song;
import xyz.michaelobi.paperplayer.injection.Injector;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.LibraryFragmentsContract;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.michaelobi.paperplayer.data.model.Song;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.LibraryFragmentsContract;

/**
 * PaperPlayer
 * Michael Obi
 * 19 10 2016 6:15 PM
 */

public class SongsFragment extends Fragment implements LibraryFragmentsContract.View<Song> {

    LibraryFragmentsContract.Presenter presenter;
    private SongsAdapter songsAdapter;
    private Context context;
    private RecyclerView recyclerViewSongs;
    private ProgressBar progressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SongsPresenter(Injector.provideMusicRepository(getContext()), Schedulers.io(),
                AndroidSchedulers.mainThread());
        context = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        View v = inflater.inflate(xyz.michaelobi.paperplayer.R.layout.fragment_songs, container, false);
        recyclerViewSongs = (RecyclerView) v.findViewById(xyz.michaelobi.paperplayer.R.id.rv_songs);
        progressBar = (ProgressBar) v.findViewById(xyz.michaelobi.paperplayer.R.id.progressbar_loading);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(context));
        songsAdapter = new SongsAdapter(null, context);
        recyclerViewSongs.setAdapter(songsAdapter);
        presenter.getAll();
        return v;
    }

    @Override
    public void showList(List<Song> songs) {
        recyclerViewSongs.setVisibility(View.VISIBLE);
        songsAdapter.setSongs(songs);
    }

    @Override
    public void showLoading() {
        recyclerViewSongs.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.detachView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
