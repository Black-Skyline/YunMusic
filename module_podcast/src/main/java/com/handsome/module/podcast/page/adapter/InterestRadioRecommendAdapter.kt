package com.handsome.module.podcast.page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.podcast.R
import com.handsome.module.podcast.databinding.ItemPodcastInterestRecommendContentBinding
import com.handsome.module.podcast.databinding.ItemPodcastInterestRecommendTitleBinding
import com.handsome.module.podcast.model.PersonalizeRecommendationData
import com.handsome.module.podcast.utils.setOnSingleClickListener

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/22
 * @Description:
 *
 */
class InterestRadioRecommendAdapter(val contentClickEvent: (PersonalizeRecommendationData.Data) -> Unit) :
    ListAdapter<InterestRadioRecommendAdapter.Data, InterestRadioRecommendAdapter.Holder>(MyDIffUtil.getNewDiff()) {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_CONTENT = 1
    }

    sealed class Data(val type: Int) {
        data class TitleBean(
            val titleText: String? = null,
            val refreshCallback: (() -> Unit)? = null,  // 可传入刷新的逻辑
            val customCallback: (() -> Unit)? = null,   // 可传入兴趣定制的逻辑
        ) : Data(TYPE_TITLE)

        data class ContentBean(val need: PersonalizeRecommendationData.Data) : Data(TYPE_CONTENT)
    }

    sealed class Holder(root: View) : RecyclerView.ViewHolder(root)

    inner class TitleHolder(binding: ItemPodcastInterestRecommendTitleBinding) :
        Holder(binding.root) {
        init {
            val callbackData = getItem(bindingAdapterPosition) as Data.TitleBean
            if (!callbackData.titleText.isNullOrBlank())
                binding.podcastTvInterestRecommendTitle.text = callbackData.titleText
            binding.podcastImgRefresh.setOnSingleClickListener(500) {
                callbackData.refreshCallback?.invoke()
            }
            binding.podcastTvInterestRecommendTitle.setOnSingleClickListener(500) {
                callbackData.refreshCallback?.invoke()
            }
            binding.podcastTvInterestRecommendCustom.setOnSingleClickListener(500) {
                callbackData.customCallback?.invoke()
            }
        }
    }

    inner class ContentHolder(val binding: ItemPodcastInterestRecommendContentBinding) :
        Holder(binding.root) {
        init {
            binding.podcastImgInterestRecommendItemBackground.setOnClickListener {
                // 拿到请求到的位于当前位置的response数据,传入PersonalizeRecommendationData.Data
                contentClickEvent((getItem(bindingAdapterPosition) as Data.ContentBean).need)
            }
            binding.podcastTvInterestRecommendItemDescription.setOnClickListener {
                // 拿到请求到的位于当前位置的response数据,传入PersonalizeRecommendationData.Data
                contentClickEvent((getItem(bindingAdapterPosition) as Data.ContentBean).need)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
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
                // getItem()类型InterestRadioRecommendAdapter.Data
                val item = (getItem(position) as Data.ContentBean).need
                holder.apply {
                    binding.podcastImgInterestRecommendItemBackground.setImageFromUrl(
                        item.picUrl, placeholder = R.drawable.ic_image_not_supported_100
                    )
                    val playNumber =
                        if (item.playCount / 100000000 > 0) "${item.playCount / 100000000}亿"
                        else if (item.playCount / 100000 > 0) "${item.playCount / 100000}万"
                        else "${item.playCount}"
                    binding.podcastTvInterestRecommendItemPlayNumber.text = playNumber
                    binding.podcastTvInterestRecommendItemDescription.text = item.rcmdText
                    binding.podcastTvInterestRecommendItemLabel.text = item.category
                }
            }
        }
    }
}