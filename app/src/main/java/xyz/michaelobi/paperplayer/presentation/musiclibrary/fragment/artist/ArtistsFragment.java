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

package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.artist;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import xyz.michaelobi.paperplayer.data.model.Artist;
import xyz.michaelobi.paperplayer.injection.Injector;

/**
 * PaperPlayer Michael Obi 21 01 2017 8:09 AM
 */

public class ArtistsFragment extends Fragment implements ArtistsView {

    ArtistsPresenter presenter;

    private Context context;
    private RecyclerView recyclerViewArtists;
    private ProgressBar progressBar;
    private ArtistsAdapter artistsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ArtistsPresenter(Injector.provideMusicRepository(getContext()));
        context = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(xyz.michaelobi.paperplayer.R.layout.fragment_artists, container, false);
        recyclerViewArtists = v.findViewById(xyz.michaelobi.paperplayer.R.id.rv_artists);
        progressBar = v.findViewById(xyz.michaelobi.paperplayer.R.id.progressbar_loading);
        recyclerViewArtists.setHasFixedSize(true);
        recyclerViewArtists.setLayoutManager(new LinearLayoutManager(context));
        artistsAdapter = new ArtistsAdapter(context);
        presenter.attachView(this);
        recyclerViewArtists.setAdapter(artistsAdapter);
        presenter.getAll();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void showList(@NonNull List<Artist> artists) {
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
    public void showError(@NonNull String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
