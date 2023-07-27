package com.handsome.module.find.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.util.extention.GONE
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.find.databinding.ItemMusicBinding
import com.handsome.module.find.network.model.MusicListDetailData
import java.lang.StringBuilder

class MusicListDetailAdapter(
    private val startMvActivity: (data: MusicListDetailData.Song) -> Unit,
    private val onClickMore : (data : MusicListDetailData.Song) -> Unit,
    private val onClick: (list: MutableList<WrapPlayInfo>, index: Int) -> Unit
) :
    PagingDataAdapter<MusicListDetailData.Song, MusicListDetailAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            //给mv注册事件
            binding.itemPictureMusicVideo.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { it1 -> startMvActivity(it1) }
            }
            //给more注册点击事件
            binding.itemMusicMore.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { it1 -> onClickMore(it1) }
            }
            //获取父布局，然后给父布局也就是整个item设置点击事件
            val viewGroup = binding.itemMusicNumber.parent as ViewGroup
            viewGroup.setOnClickListener {
                val list = ArrayList<WrapPlayInfo>()
                var wrapPlayInfo: WrapPlayInfo?
                //取点的item的前十个和后十个，每次点击每次刷新
                val beforeIndex =
                    if (bindingAdapterPosition - 10 >= 0) bindingAdapterPosition - 10 else 0
                val afterIndex =
                    if (bindingAdapterPosition + 10 <= itemCount) bindingAdapterPosition + 10 else itemCount
                for (i in beforeIndex until afterIndex) {
                    val song = getItem(i)
                    if (song != null) {
                        val audioName = song.name
                        val artist = StringBuilder()
                        for (j in song.ar) {
                            artist.append(j.name).append("  ")
                        }
                        val songId = song.id
                        val picUrl = song.al.picUrl
                        wrapPlayInfo = WrapPlayInfo(audioName, artist.toString(), songId, picUrl)
                        list.add(wrapPlayInfo)
                    }
                }
                onClick(list, bindingAdapterPosition - beforeIndex)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemMusicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        val number = (position + 1).toString()
        if (item != null) {
            holder.binding.apply {
                itemMusicNumber.text = number
                itemMusicName.text = item.name
                itemMusicSinger.text = item.ar[0].name
                if (item.mv == 0L) itemPictureMusicVideo.GONE()
            }
        }
    }
}