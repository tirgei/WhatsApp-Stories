package com.gelostech.whatsappstories.commoners

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import java.io.File

object AppUtils {

    fun setDrawable(context: Context, icon: IIcon, color: Int, size: Int): Drawable {
        return IconicsDrawable(context).icon(icon).color(ContextCompat.getColor(context, color)).sizeDp(size)
    }

    fun isImage(file: File): Boolean {
        val fileName = file.name.toLowerCase()
        return fileName.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png")
    }

    fun isVideo(file: File): Boolean {
        val fileName = file.name.toLowerCase()
        return fileName.endsWith("mp4") || fileName.endsWith("avi") || fileName.endsWith("gif") || fileName.endsWith("mkv")
    }


}