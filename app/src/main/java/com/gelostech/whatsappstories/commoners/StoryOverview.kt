package com.gelostech.whatsappstories.commoners

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.gelostech.whatsappstories.R
import com.gelostech.whatsappstories.activities.ImageActivity
import com.gelostech.whatsappstories.activities.VideoActivity
import com.gelostech.whatsappstories.models.RefreshStoriesEvent
import com.gelostech.whatsappstories.models.Story
import com.gelostech.whatsappstories.utils.hideView
import com.gelostech.whatsappstories.utils.loadUrl
import com.gelostech.whatsappstories.utils.setDrawable
import com.gelostech.whatsappstories.utils.showView
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.overview_story.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import java.io.File

class StoryOverview : Dialog, View.OnClickListener {
    private var story: Story
    private var c: Context
    private var showDeleteButton: Boolean

    constructor(context: Context, story: Story, showDeleteButton: Boolean = false): super(context) {
        this.c = context
        this.story = story
        this.showDeleteButton = showDeleteButton
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.overview_story)

        view.setDrawable(AppUtils.setDrawable(c, Ionicons.Icon.ion_eye, R.color.secondaryText, 15))
        share.setDrawable(AppUtils.setDrawable(c, Ionicons.Icon.ion_share, R.color.secondaryText, 15))

        view.setOnClickListener(this)
        share.setOnClickListener(this)
        media.setOnClickListener(this)

        if (showDeleteButton) {
            save.hideView()
            delete.showView()

            delete.setDrawable(AppUtils.setDrawable(c, Ionicons.Icon.ion_ios_trash, R.color.secondaryText, 15))
            delete.setOnClickListener(this)

        } else {
            delete.hideView()
            save.showView()

            save.setDrawable(AppUtils.setDrawable(c, Ionicons.Icon.ion_android_download, R.color.secondaryText, 15))
            save.setOnClickListener(this)
        }

        when(story.type) {
            K.TYPE_IMAGE -> loadImageStory()
            K.TYPE_VIDEO -> loadVideoStory()
        }

        this.setOnDismissListener {
            JZVideoPlayer.releaseAllVideos()
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
            R.id.view, R.id.media -> {
                when(story.type) {
                    K.TYPE_IMAGE -> {
                        val i = Intent(c, ImageActivity::class.java)
                        i.putExtra(K.STORY, story)
                        c.startActivity(i)
                    }

                    K.TYPE_VIDEO -> {
                        val i = Intent(c, VideoActivity::class.java)
                        i.putExtra(K.STORY, story)
                        c.startActivity(i)
                    }
                }
            }

            R.id.share -> {
                when(story.type) {
                    K.TYPE_IMAGE -> {
                        val image = BitmapFactory.decodeFile(story.path,BitmapFactory.Options())
                        AppUtils.shareImage(c,image)
                    }

                    K.TYPE_VIDEO -> {
                        AppUtils.shareVideo(c, story.path!!)
                    }
                }
            }

            R.id.save -> {
                when(story.type) {
                    K.TYPE_IMAGE -> {
                        val image = BitmapFactory.decodeFile(story.path,BitmapFactory.Options())
                        AppUtils.saveImage(c,image)
                    }

                    K.TYPE_VIDEO -> {
                        AppUtils.saveVideo(c, story.path!!)
                    }
                }

                EventBus.getDefault().post(RefreshStoriesEvent())
            }

            R.id.delete -> {
                context.alert("Are you sure you want to delete this story?") {
                    title = "Delete story"

                    positiveButton("Delete") {
                        File(story.path!!).absoluteFile.delete()
                        context.toast("Story deleted")
                        dismiss()

                        EventBus.getDefault().post(RefreshStoriesEvent())
                    }

                    negativeButton("Cancel") {}
                }.show()
            }
        }
    }

}