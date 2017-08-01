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

package xyz.michaelobi.paperplayer.presentation.musiclibrary

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View

import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener

import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.event.EventBus
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.playback.PlaybackService
import xyz.michaelobi.paperplayer.playback.events.PlaybackState
import xyz.michaelobi.paperplayer.playback.events.action.RequestPlaybackState
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.albums.AlbumsFragment
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.artist.ArtistsFragment
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs.SongsFragment

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:36 PM
 */

class MusicLibraryActivity : AppCompatActivity(), MusicLibraryContract.View, View.OnClickListener, PermissionListener {

    lateinit var musicLibraryPresenter: MusicLibraryContract.Presenter

    private var bus: EventBus? = Injector.provideEventBus()

    private var fab: FloatingActionButton? = null

    override fun onResume() {
        super.onResume()
        setUpEventBus()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        musicLibraryPresenter = MusicLibraryPresenter()
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        fab = findViewById(R.id.fab) as FloatingActionButton
        fab!!.setOnClickListener(this)
        val listener = CompositePermissionListener(this,
                DialogOnDeniedPermissionListener.Builder
                        .withContext(this)
                        .withTitle("Storage permission")
                        .withMessage("Permission to access your storage is needed to play music")
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_player)
                        .build())

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(listener)
                .check()
    }

    override fun onDestroy() {
        super.onDestroy()
        bus = null
    }

    private fun setUpEventBus() {
        bus!!.observe(PlaybackState::class.java)
                .subscribe { playbackState ->
                    if (playbackState.playing) {
                        fab!!.setImageResource(R.drawable.ic_play_arrow_white_24dp)
                    } else {
                        fab!!.setImageResource(R.drawable.ic_pause_white_24dp)
                    }
                }
        bus!!.post(RequestPlaybackState())
    }

    override fun initializeViewPager() {
        val mViewPager = findViewById(R.id.container) as ViewPager
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(SongsFragment(), resources.getString(R.string.songs))
        viewPagerAdapter.addFragment(AlbumsFragment(), resources.getString(R.string.albums))
        viewPagerAdapter.addFragment(ArtistsFragment(), resources.getString(R.string.artists))
        if (mViewPager != null) {
            mViewPager.adapter = viewPagerAdapter
            mViewPager.currentItem = 0
        }

        val tabLayout = findViewById(R.id.main_tablayout) as TabLayout
        if (tabLayout != null) {
            tabLayout.tabMode = TabLayout.MODE_FIXED
            tabLayout.setupWithViewPager(mViewPager)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> musicLibraryPresenter.onFabClick()
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse) {
        musicLibraryPresenter.attachView(this)
        applicationContext.startService(Intent(applicationContext, PlaybackService::class.java))
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse) {
        finish()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
        token.continuePermissionRequest()
    }
}
