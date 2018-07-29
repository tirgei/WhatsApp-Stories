package com.gelostech.whatsappstories.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.whatsappstories.R
import com.gelostech.whatsappstories.adapters.StoriesAdapter
import com.gelostech.whatsappstories.callbacks.StoryCallback
import com.gelostech.whatsappstories.commoners.AppUtils.isImage
import com.gelostech.whatsappstories.commoners.AppUtils.isVideo
import com.gelostech.whatsappstories.commoners.BaseFragment
import com.gelostech.whatsappstories.commoners.K
import com.gelostech.whatsappstories.models.Story
import com.gelostech.whatsappstories.utils.RecyclerFormatter
import com.gelostech.whatsappstories.utils.hideView
import com.gelostech.whatsappstories.utils.showView
import kotlinx.android.synthetic.main.fragment_saved.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class SavedFragment : BaseFragment(), StoryCallback {
    private lateinit var adapter: StoriesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        loadStories()
    }

    private fun initViews() {
        rv.setHasFixedSize(true)
        rv.layoutManager = GridLayoutManager(activity!!, 3)
        rv.addItemDecoration(RecyclerFormatter.GridItemDecoration(activity!!, 3, 5))
        rv.itemAnimator = DefaultItemAnimator()
        (rv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        adapter = StoriesAdapter(this, activity!!)
        rv.adapter = adapter

    }

    private fun loadStories() {
        if (!storagePermissionGranted()) {
            requestStoragePermission()
            return
        }

        val dir = File(K.SAVED_STORIES)
        if (!dir.exists())
            dir.mkdirs()

        doAsync {
            val files = dir.listFiles { _, s ->
                s.endsWith(".png") || s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".mp4") || s.endsWith(".gif") }

            uiThread {

                if (files.isNotEmpty()) {
                    hasStories()
                    var story = Story()

                    for (file in files.sortedBy { it.lastModified() }.reversed()) {
                        if (isImage(file)) {
                            story = Story(K.TYPE_IMAGE, file.absolutePath)
                        } else if (isVideo(file)) {
                            story = Story(K.TYPE_VIDEO, file.absolutePath)
                        }

                        adapter.addStory(story)
                    }

                } else {
                    noStories()
                }
            }

        }

    }

    private fun noStories() {
        rv?.hideView()
        empty?.showView()
    }

    private fun hasStories() {
        empty?.hideView()
        rv?.showView()
    }

    override fun onStoryClicked(v: View, story: Story) {

    }

}
