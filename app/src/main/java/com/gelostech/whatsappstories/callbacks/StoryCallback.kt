package com.gelostech.whatsappstories.callbacks

import android.view.View
import com.gelostech.whatsappstories.models.Story

interface StoryCallback {

    fun onStoryClicked(v: View, story: Story)

}