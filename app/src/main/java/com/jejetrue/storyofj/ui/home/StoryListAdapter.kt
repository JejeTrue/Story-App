package com.jejetrue.storyofj.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storysubmissionapp.data.model.Story

import com.jejetrue.storyofj.databinding.ItemStoryBinding
import com.jejetrue.storyofj.loadImage
import com.jejetrue.storyofj.ui.detail.DetailActivity

class StoryListAdapter : PagingDataAdapter<Story ,StoryListAdapter.MyViewHolder>(DIFF_CALLBACK){
    inner class MyViewHolder( val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(story: Story){
            binding.apply {
                tvNama.text = story.name
                tvDesc.text = story.description
                ivStory.loadImage(itemView.context, story.photoUrl)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.ID, story.id)

                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.tvNama as View, "name"),
                    Pair(binding.tvDesc as View , "description"),
                    Pair(binding.ivStory as View, "photo")
                )
                itemView.context.startActivity(intent,optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


}