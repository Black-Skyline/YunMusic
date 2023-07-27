package com.handsome.module.podcast.page.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.podcast.R
import com.handsome.module.podcast.databinding.ItemPodcastNormalRecommendContentBinding
import com.handsome.module.podcast.model.NormalRecommendationData.DjRadio

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/22
 * @Description:
 *
 */
class NormalRecommendAdapter(val contentClickEvent: (DjRadio) -> Unit) :
    ListAdapter<DjRadio, NormalRecommendAdapter.Holder>(MyDIffUtil.getNewDiff()) {
    inner class Holder(val binding: ItemPodcastNormalRecommendContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.podcastImgNormalRecommendItemBackground.setOnClickListener {
                // 拿到请求到的位于当前位置的response数据,传入NormalRecommendationData.DjRadio
                contentClickEvent(getItem(bindingAdapterPosition))
            }
            binding.podcastTvNormalRecommendItemDescription.setOnClickListener {
                // 拿到请求到的位于当前位置的response数据,传入NormalRecommendationData.DjRadio
                contentClickEvent(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemPodcastNormalRecommendContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)

        holder.apply {
            binding.podcastImgNormalRecommendItemBackground.setImageFromUrl(
                item.picUrl, placeholder = R.drawable.ic_image_not_supported_100
            )
            val playNumber =
                if (item.playCount / 100000000 > 0) "${item.playCount / 100000000}亿"
                else if (item.playCount / 100000 > 0) "${item.playCount / 100000}万"
                else "${item.playCount}"
            binding.podcastTvNormalRecommendItemPlayNumber.text = playNumber
            binding.podcastTvNormalRecommendItemDescription.text = item.rcmdtext
        }
    }
}