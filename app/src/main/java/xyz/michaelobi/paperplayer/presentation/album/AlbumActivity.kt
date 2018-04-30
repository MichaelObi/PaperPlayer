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

package xyz.michaelobi.paperplayer.presentation.album

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.data.model.Song
import xyz.michaelobi.paperplayer.databinding.AlbumActivityBinding
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs.SongsAdapter
import java.io.File


/**
 * PaperPlayer
 * Michael Obi
 * 06 09 2017 7:37 PM
 */

class AlbumActivity : AppCompatActivity(), AlbumContract.View {

    private lateinit var presenter: AlbumContract.Presenter
    private lateinit var viewBinding: AlbumActivityBinding
    private lateinit var songsAdapter: SongsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val albumId = intent.getLongExtra(KEY_ALBUM_ID, 0)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.album_activity)
        viewBinding.toolbar.setNavigationIcon(R.drawable.ic_close_24dp)
        viewBinding.toolbar.title = ""
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter = AlbumPresenter(Injector.provideMusicRepository(this), Schedulers.io(),
                AndroidSchedulers.mainThread())
        presenter.attachView(this)
        songsAdapter = SongsAdapter(null, this)
        viewBinding.rvSongs.adapter = songsAdapter
        viewBinding.rvSongs.layoutManager = LinearLayoutManager(this)
        viewBinding.rvSongs.isNestedScrollingEnabled = false
        presenter.loadAlbum(albumId)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return true
    }

    override fun showAlbumArt(albumArt: File) {
        Picasso.with(this).load(albumArt)
                .config(Bitmap.Config.RGB_565)
                .error(R.drawable.default_artwork_dark)
                .into(viewBinding.albumArt)
    }

    override fun showReleaseYear(year: String?) {
        viewBinding.year.visibility = View.VISIBLE
        viewBinding.year.text = getString(R.string.year_released, year)
    }

    override fun showSongCount(numberOfSongs: Int) {
        viewBinding.songCount.visibility = View.VISIBLE
        viewBinding.songCount.text = getString(R.string.song_count, numberOfSongs)
    }

    override fun showAlbumSongList(songs: List<Song>) {
        viewBinding.rvSongs.visibility = View.VISIBLE
        songsAdapter.setSongs(songs)
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val KEY_ALBUM_ID = "ALBUM_ID"
    }
}