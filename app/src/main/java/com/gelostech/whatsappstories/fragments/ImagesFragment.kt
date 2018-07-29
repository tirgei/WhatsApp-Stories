package com.gelostech.whatsappstories.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.whatsappstories.R
import com.gelostech.whatsappstories.Story
import com.gelostech.whatsappstories.adapters.StoriesAdapter
import com.gelostech.whatsappstories.callbacks.StoryCallback
import com.gelostech.whatsappstories.commoners.BaseFragment
import com.gelostech.whatsappstories.commoners.K
import com.gelostech.whatsappstories.utils.RecyclerFormatter
import kotlinx.android.synthetic.main.fragment_images.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.FilenameFilter
import java.nio.file.Files.exists



class ImagesFragment : BaseFragment(), StoryCallback {
    private lateinit var adapter: StoriesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_images, container, false)
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

        adapter = StoriesAdapter(this)
        rv.adapter = adapter

    }

    private fun loadStories() {
        if (!storagePermissionGranted()) {
            requestStoragePermission()
            return
        }

        val dir = File(K.WHATSAPP_STORIES)
        if (!dir.exists())
            dir.mkdirs()

        val files = dir.listFiles { _, s ->
            s.endsWith(".png") || s.endsWith(".jpg") || s.endsWith(".jpeg") }

        if (files.isNotEmpty()) {

            for (file in files) {
                val story = Story(K.TYPE_IMAGE, file.absolutePath)
                adapter.addStory(story)
            }

        }

    }

    override fun onStoryClicked(v: View, story: Story) {

    }
}
