package com.gelostech.whatsappstories.commoners

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import cn.jzvd.JZVideoPlayerStandard
import com.gelostech.whatsappstories.R
import com.gelostech.whatsappstories.activities.ImageActivity
import com.gelostech.whatsappstories.models.Story
import com.gelostech.whatsappstories.utils.loadUrl
import com.gelostech.whatsappstories.utils.setDrawable
import com.gelostech.whatsappstories.utils.showView
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.overview_story.*
import org.jetbrains.anko.toast

class StoryOverview : Dialog, View.OnClickListener {
    private lateinit var story: Story
    private lateinit var c: Context

    constructor(context: Context, story: Story): super(context) {
        this.c = context
        this.story = story
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.overview_story)

        view.setDrawable(AppUtils.setDrawable(c, Ionicons.Icon.ion_eye, R.color.secondaryText, 15))
        share.setDrawable(AppUtils.setDrawable(c, Ionicons.Icon.ion_share, R.color.secondaryText, 15))
        save.setDrawable(AppUtils.setDrawable(c, Ionicons.Icon.ion_android_download, R.color.secondaryText, 15))

        view.setOnClickListener(this)
        share.setOnClickListener(this)
        save.setOnClickListener(this)

        when(story.type) {
            K.TYPE_IMAGE -> loadImageStory()

            K.TYPE_VIDEO -> loadVideoStory()
        }

    }

    private fun loadImageStory() {
        image?.showView()
        image?.loadUrl(story.path!!)
    }

    private fun loadVideoStory() {
        video?.showView()
        video?.setUp(story.path, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "")
        video?.thumbImageView?.loadUrl(story.path!!)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.view -> {
                when(story.type) {
                    K.TYPE_IMAGE -> {
                        val i = Intent(c, ImageActivity::class.java)
                        i.putExtra(K.STORY, story)
                        c.startActivity(i)
                    }

                    K.TYPE_VIDEO -> {

                    }
                }
            }

            R.id.share -> {

            }

            R.id.save -> {

            }
        }
    }
}