package com.gelostech.whatsappstories.commoners

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.support.v4.content.ContextCompat
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import org.apache.commons.io.FileUtils
import org.jetbrains.anko.toast
import timber.log.Timber
import java.io.*
import java.nio.channels.FileChannel


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

    fun saveImage(context: Context, bitmap: Bitmap) {
        val file = File(K.SAVED_STORIES)
        if(!file.exists()) file.mkdirs()

        val fileName = "Story-" + System.currentTimeMillis() + ".jpg"

        val newImage = File(file, fileName)
        if(newImage.exists()) file.delete()
        try {
            val out = FileOutputStream(newImage)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()

            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context, arrayOf(newImage.absolutePath), null, null)
            } else {
                context.sendBroadcast( Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(newImage)))
            }
            context.toast("Story saved")

        } catch (e: Exception){
            Timber.e(e.localizedMessage)
        }

    }

    fun saveVideo(context: Context, filePath: String) {
        val sourceFile = File(filePath)
        val destinationFile = File(K.SAVED_STORIES, sourceFile.name)

        FileUtils.copyFile(sourceFile, destinationFile, false)
        context.toast("Story saved")
    }

    fun shareImage(context: Context, bitmap: Bitmap) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/*"

        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val f = File("${Environment.getExternalStorageDirectory()}/${File.separator}/temporary_file.jpg")

        try {
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if(Build.VERSION.SDK_INT>=24){
            try{
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            }catch(e: Exception){
                e.printStackTrace()
            }
        }

        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"))
        context.startActivity(Intent.createChooser(share, "Share via..."))
    }

    fun shareVideo(context: Context, path: String) {
        val intent = Intent(Intent.ACTION_SEND, Uri.parse(path))
        intent.setDataAndType(Uri.parse(path), "video/*")
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
        context.startActivity(Intent.createChooser(intent, "Share via..."))
    }

}