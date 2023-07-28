package com.handsome.module.podcast.page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.podcast.databinding.ItemProgramsListContentBinding
import com.handsome.module.podcast.model.RadioProgramsData
import java.lang.StringBuilder

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */
class ProgramsDisplayAdapter(
    private val onClickMore: (data: RadioProgramsData.Program) -> Unit,
    private val onClick: (list: MutableList<WrapPlayInfo>, index: Int) -> Unit
) : PagingDataAdapter<RadioProgramsData.Program, ProgramsDisplayAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding: ItemProgramsListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            // more的点击事件
            binding.itemAudioContentImgMore.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { onClickMore(it) }
            }
            // 给整个item设置点击事件
            binding.itemAudioContentAll.setOnClickListener {
                val list = ArrayList<WrapPlayInfo>()
                var wrapPlayInfo: WrapPlayInfo?
                // 取被点击的item的前二十个和后二十个加载，每次点击item每次刷新
                val beforeIndex =
                    if (bindingAdapterPosition - 20 >= 0) bindingAdapterPosition - 20 else 0
                val afterIndex =
                    if (bindingAdapterPosition + 20 <= itemCount) bindingAdapterPosition + 20 else itemCount
                for (i in beforeIndex until afterIndex) {
                    val program = getItem(i)
                    if (program != null) {
                        val audioName = program.name
                        val artist = program.radio.name
                        val audioId = program.mainSong.id
                        val picUrl = program.coverUrl
                        wrapPlayInfo = WrapPlayInfo(audioName, artist, audioId, picUrl)
                        list.add(wrapPlayInfo)
                    }
                }
                onClick(list, bindingAdapterPosition - beforeIndex)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemProgramsListContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.binding.apply {
                itemAudioContentImgPicture.setImageFromUrl(item.coverUrl)
                itemAudioContentTvName.text = item.name
                itemAudioContentTvOtherInfo.text = "播放量：${item.listenerCount}"
            }
        }
    }
}