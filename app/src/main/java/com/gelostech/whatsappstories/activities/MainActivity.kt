package com.gelostech.whatsappstories.activities

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import cn.jzvd.JZVideoPlayer
import com.gelostech.whatsappstories.R
import com.gelostech.whatsappstories.commoners.BaseActivity
import com.gelostech.whatsappstories.fragments.ImagesFragment
import com.gelostech.whatsappstories.fragments.SavedFragment
import com.gelostech.whatsappstories.fragments.VideosFragment
import com.gelostech.whatsappstories.utils.PagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity(), TabLayout.OnTabSelectedListener {
    private var doubleBackToExit = false

    companion object {
        private const val IMAGES = "IMAGES"
        private const val VIDEOS = "VIDEOS"
        private const val SAVED = "SAVED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        if (!storagePermissionGranted()) requestStoragePermission()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        setupViewPager()
        setupTabs()
    }

    private fun setupViewPager() {
        val adapter = PagerAdapter(supportFragmentManager, this)
        val images = ImagesFragment()
        val videos = VideosFragment()
        val saved = SavedFragment()

        adapter.addAllFrags(images, videos, saved)
        adapter.addAllTitles(IMAGES, VIDEOS, SAVED)

        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 2
        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

    }

    private fun setupTabs() {
        tabs.setupWithViewPager(viewpager)
        tabs.addOnTabSelectedListener(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewpager.setCurrentItem(tab!!.position, true)
    }

    override fun onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return
        }

        if (doubleBackToExit) {
            super.onBackPressed()
        } else {
            toast("Please tap back again to exit")

            doubleBackToExit = true

            Handler().postDelayed({doubleBackToExit = false }, 1500)
        }
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

}
