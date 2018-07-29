package com.gelostech.whatsappstories.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.gelostech.whatsappstories.R
import com.gelostech.whatsappstories.commoners.AppUtils
import com.gelostech.whatsappstories.commoners.AppUtils.setDrawable
import com.gelostech.whatsappstories.commoners.K
import com.gelostech.whatsappstories.models.Story
import com.gelostech.whatsappstories.utils.loadUrl
import com.gelostech.whatsappstories.utils.setDrawable
import com.gelostech.whatsappstories.utils.showView
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.overview_story.*
import kotlinx.android.synthetic.main.overview_story.view.*

class OverviewFragment : DialogFragment(), View.OnClickListener{
    private lateinit var story: Story
    private lateinit var builder: AlertDialog.Builder
    private lateinit var c: Context

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        c = activity!!

        builder = AlertDialog.Builder(c)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.overview_story, null)

        view.view.setDrawable(setDrawable(c, Ionicons.Icon.ion_eye, R.color.secondaryText, 15))
        view.share.setDrawable(setDrawable(c, Ionicons.Icon.ion_share, R.color.secondaryText, 15))
        view.save.setDrawable(setDrawable(c, Ionicons.Icon.ion_android_download, R.color.secondaryText, 15))

//        view.view.setOnClickListener(this)
//        view.share.setOnClickListener(this)
//        view.save.setOnClickListener(this)

        story = arguments?.getSerializable(K.STORY) as Story

        when(story.type) {
            K.TYPE_IMAGE -> loadImageStory(view)
            K.TYPE_VIDEO -> loadVideoStory(view)
        }

        builder.show()

        builder.setView(view)
        return builder.create()
    }

    private fun loadImageStory(v: View) {
        v.image.showView()
        v.image.loadUrl(story.path!!)
    }

    private fun loadVideoStory(v: View) {
        v.video.showView()
        v.video.setUp(story.path, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "")
        v.video.thumbImageView.loadUrl(story.path!!)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.view -> {
                view()
            }

            R.id.share -> {
                share()
            }

            R.id.save -> {
                save()
            }
        }
    }

    private fun view() {

    }

    private fun share() {
        val image = (view?.image!!.drawable as BitmapDrawable).bitmap

        AppUtils.shareImage(c, image)
    }

    private fun save() {
        val image = (view?.image!!.drawable as BitmapDrawable).bitmap

        AppUtils.saveImage(c, image)
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

}