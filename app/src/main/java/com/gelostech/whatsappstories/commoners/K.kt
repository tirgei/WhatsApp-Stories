package com.gelostech.whatsappstories.commoners

import android.os.Environment

object K {

    const val TYPE_IMAGE = 0
    const val TYPE_VIDEO = 1

    var WHATSAPP_STORIES = Environment.getExternalStorageDirectory().absolutePath +"/WhatsApp/Media/.Statuses"
    var SAVED_STORIES = Environment.getExternalStorageDirectory().absolutePath +"/WhatsApp Stories"

}