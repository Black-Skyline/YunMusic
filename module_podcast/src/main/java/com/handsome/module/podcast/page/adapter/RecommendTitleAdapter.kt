package com.handsome.module.podcast.page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.handsome.module.podcast.databinding.ItemPodcastRecommendTitleTextBinding

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */
class RecommendTitleAdapter(private val titleText: String) :
    RecyclerView.Adapter<RecommendTitleAdapter.Holder>() {
    class Holder(val binding: ItemPodcastRecommendTitleTextBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemPodcastRecommendTitleTextBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.podcastTvNormalRecommendTitle.text = titleText
    }

}