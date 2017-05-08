package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.artist;

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
import xyz.michaelobi.paperplayer.data.model.Artist;
import xyz.michaelobi.paperplayer.injection.Injector;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.LibraryFragmentsContract;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.LibraryFragmentsContract;

/**
 * PaperPlayer Michael Obi 21 01 2017 8:09 AM
 */

public class ArtistsFragment extends Fragment implements LibraryFragmentsContract.View<Artist> {

    LibraryFragmentsContract.Presenter presenter;

    private Context context;
    private RecyclerView recyclerViewArtists;
    private ProgressBar progressBar;
    private ArtistsAdapter artistsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ArtistsPresenter(Injector.provideMusicRepository(getContext()), Schedulers.io(),
                AndroidSchedulers.mainThread());
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(xyz.michaelobi.paperplayer.R.layout.fragment_artists, container, false);
        recyclerViewArtists = (RecyclerView) v.findViewById(xyz.michaelobi.paperplayer.R.id.rv_artists);
        progressBar = (ProgressBar) v.findViewById(xyz.michaelobi.paperplayer.R.id.progressbar_loading);
        presenter.attachView(this);
        recyclerViewArtists.setHasFixedSize(true);
        recyclerViewArtists.setLayoutManager(new LinearLayoutManager(context));
        artistsAdapter = new ArtistsAdapter(context);
        recyclerViewArtists.setAdapter(artistsAdapter);
        presenter.getAll();
        return v;
    }

    @Override
    public void showList(List<Artist> artists) {
        recyclerViewArtists.setVisibility(View.VISIBLE);
        artistsAdapter.setArtists(artists);
    }

    @Override
    public void showLoading() {
        recyclerViewArtists.setVisibility(View.GONE);
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
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
