package com.handsome.module.podcast.page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.podcast.R
import com.handsome.module.podcast.databinding.ItemPodcastInterestRecommendContentBinding
import com.handsome.module.podcast.databinding.ItemPodcastInterestRecommendTitleBinding
import com.handsome.module.podcast.model.PersonalizeRecommendationData

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/22
 * @Description:
 *
 */
class InterestRadioRecommendAdapter(
    val titleClickEvent: (() -> Unit)? = null,
    val contentClickEvent: (PersonalizeRecommendationData.Data) -> Unit
) :
    ListAdapter<InterestRadioRecommendAdapter.Data, InterestRadioRecommendAdapter.Holder>(MyDIffUtil.getNewDiff()) {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_CONTENT = 1
    }

    sealed class Data(val type: Int) {
        data class TitleBean(val titleText: String = "") : Data(TYPE_TITLE)
        data class ContentBean(val need: PersonalizeRecommendationData.Data) : Data(TYPE_CONTENT)
    }

    sealed class Holder(root: View) : ViewHolder(root)

    inner class TitleHolder(val binding: ItemPodcastInterestRecommendTitleBinding) :
        Holder(binding.root) {

    }

    inner class ContentHolder(val binding: ItemPodcastInterestRecommendContentBinding) :
        Holder(binding.root) {
            init {
                binding.itemPodcastRecommendBackgroundImg.setOnClickListener {
//                    contentClickEvent(getItem())
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return when (viewType) {
            TYPE_TITLE -> TitleHolder(
                ItemPodcastInterestRecommendTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> ContentHolder(
                ItemPodcastInterestRecommendContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        when (holder) {
            is TitleHolder -> {  //position == 0
                holder.apply {
                    // 暂时没有对其操作的需求
                }
            }

            is ContentHolder -> {  //position != 0
                val item =
                    (getItem(position) as Data.ContentBean).need // getItem()类型InterestRadioRecommendAdapter.Data
                holder.apply {
                    binding.itemPodcastRecommendBackgroundImg.setImageFromUrl(
                        item.picUrl, placeholder = R.drawable.ic_image_not_supported_100
                    )
                    val playNumber =
                        if (item.playCount / 100000000 > 0) "${item.playCount / 100000000}亿"
                        else if (item.playCount / 100000 > 0) "${item.playCount / 100000}亿"
                        else "${item.playCount}"
                    binding.itemPodcastRecommendBackgroundNumber.text = playNumber
                    binding.itemPodcastRecommendDescription.text = item.rcmdText
                    binding.itemPodcastRecommendLabel.text = item.category
                }
            }
        }
    }

}