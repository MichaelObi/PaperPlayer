package net.devdome.paperplayer.presentation.musiclibrary.songs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Song;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * PaperPlayer
 * Michael Obi
 * 19 10 2016 6:15 PM
 */

public class SongsFragment extends Fragment implements SongsContract.View {

    SongsContract.Presenter songsPresenter;
    private SongsAdapter songsAdapter;
    private Context context;
    private RecyclerView recyclerViewSongs;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songsPresenter = new SongsPresenter(Schedulers.io(), AndroidSchedulers.mainThread());
        songsPresenter.attachView(this);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerViewSongs = (RecyclerView) v.findViewById(R.id.rv_songs);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(context));
        songsAdapter = new SongsAdapter(null, context);
        recyclerViewSongs.setAdapter(songsAdapter);
        songsPresenter.getAllSongs();
        return v;
    }

    @Override
    public void showSongsList(List<Song> songs) {
        recyclerViewSongs.setVisibility(View.VISIBLE);
        songsAdapter.setSongs(songs);
    }

    @Override
    public void showLoading() {
        Toast.makeText(getContext(), "Loading Library", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        songsPresenter.detachView();
        super.onDestroyView();
    }
}
