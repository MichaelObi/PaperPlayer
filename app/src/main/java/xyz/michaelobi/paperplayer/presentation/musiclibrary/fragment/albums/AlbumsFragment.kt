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

package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.albums

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.data.model.Album
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.mvp.ListViewContract

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 4:00 PM
 */

class AlbumsFragment : Fragment(), ListViewContract.View<Album> {

    lateinit var presenter: ListViewContract.Presenter
    lateinit private var albumsAdapter: AlbumsAdapter
    lateinit private var recyclerViewAlbums: RecyclerView
    lateinit private var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AlbumsPresenter(Injector.provideMusicRepository(activity), Schedulers.io(),
                AndroidSchedulers.mainThread())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_albums, container, false)
        recyclerViewAlbums = v.findViewById(xyz.michaelobi.paperplayer.R.id.rv_albums_grid) as RecyclerView
        progressBar = v.findViewById(xyz.michaelobi.paperplayer.R.id.progressbar_loading) as ProgressBar
        presenter.attachView(this)
        albumsAdapter = AlbumsAdapter(null, activity)
        with(recyclerViewAlbums) {
            layoutManager = GridLayoutManager(context, calculateNoOfColumns())
            setHasFixedSize(true)
            adapter = albumsAdapter
        }
        presenter.getAll()
        return v
    }

    private fun calculateNoOfColumns(): Int {
        val displayMetrics = activity.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        return (dpWidth / 180).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun showList(albums: List<Album>) {
        recyclerViewAlbums.visibility = View.VISIBLE
        albumsAdapter.setAlbums(albums)
    }

    override fun showLoading() {
        recyclerViewAlbums.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
