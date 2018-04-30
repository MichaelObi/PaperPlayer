/*
 * MIT License
 *
 * Copyright (c) 2017 MIchael Obi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import xyz.michaelobi.paperplayer.data.model.Song;
import xyz.michaelobi.paperplayer.injection.Injector;
import xyz.michaelobi.paperplayer.mvp.ListViewContract;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * PaperPlayer
 * Michael Obi
 * 19 10 2016 6:15 PM
 */

public class SongsFragment extends Fragment implements ListViewContract.View<Song> {

    ListViewContract.Presenter presenter;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(xyz.michaelobi.paperplayer.R.layout.fragment_songs, container, false);
        recyclerViewSongs = v.findViewById(xyz.michaelobi.paperplayer.R.id.rv_songs);
        progressBar = v.findViewById(xyz.michaelobi.paperplayer.R.id.progressbar_loading);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(context));
        songsAdapter = new SongsAdapter();
        presenter.attachView(this);
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
