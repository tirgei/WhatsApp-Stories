package com.gelostech.whatsappstories.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.gelostech.whatsappstories.R
import com.gelostech.whatsappstories.Story
import com.gelostech.whatsappstories.callbacks.StoryCallback
import com.gelostech.whatsappstories.commoners.K
import com.gelostech.whatsappstories.databinding.ItemImageBinding
import com.gelostech.whatsappstories.utils.inflate

class StoriesAdapter(private val callback: StoryCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val stories = mutableListOf<Story>()

    fun addStory(story: Story) {
        stories.add(story)
        notifyItemInserted(stories.size)
    }

    fun addStories(stories: List<Story>) {
        this.stories.addAll(stories)
        notifyDataSetChanged()
    }

    fun clearStories() {
        stories.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            K.TYPE_IMAGE -> {
                ImageHolder(parent.inflate(R.layout.item_image), callback)
            }

            else -> {
                VideoHolder(parent.inflate(R.layout.item_image))
            }
        }
    }

    override fun getItemCount(): Int = stories.size

    override fun getItemViewType(position: Int): Int {
        return stories[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ImageHolder -> holder.bind(stories[position])
            is VideoHolder -> holder.bind(stories[position])
        }
    }

    class ImageHolder(private val binding: ItemImageBinding, private val callback: StoryCallback): RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            binding.story = story
        }

    }

    class VideoHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            binding.story = story
        }

    }

}