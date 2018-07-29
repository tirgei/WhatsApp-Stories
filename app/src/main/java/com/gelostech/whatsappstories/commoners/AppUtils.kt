package com.gelostech.whatsappstories.commoners

import java.io.File

object AppUtils {

    fun isImage(file: File): Boolean {
        val fileName = file.name.toLowerCase()
        return fileName.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png")
    }

    fun isVideo(file: File): Boolean {
        val fileName = file.name.toLowerCase()
        return fileName.endsWith("mp4") || fileName.endsWith("avi") || fileName.endsWith("gif") || fileName.endsWith("mkv")
    }


}