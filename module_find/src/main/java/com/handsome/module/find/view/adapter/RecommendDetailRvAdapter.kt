package com.handsome.module.find.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.util.extention.GONE
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.find.databinding.ItemPictureMusic1Binding
import com.handsome.module.find.network.model.RecommendDetailData
import java.lang.StringBuilder

class RecommendDetailRvAdapter(private val onClick: (list: MutableList<WrapPlayInfo>, index: Int) -> Unit) :
    ListAdapter<RecommendDetailData.Data.DailySong, RecommendDetailRvAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding: ItemPictureMusic1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            //获取父布局，然后给父布局也就是整个item设置点击事件
            val viewGroup = binding.itemPictureMusicName.parent as ViewGroup
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
                    if (song!=null){
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
            ItemPictureMusic1Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            itemPictureMusicImg.setImageFromUrl(item.al.picUrl)
            itemPictureMusicName.text = item.name
            val singerName =" ${item.reason?:""} ${item.ar[0].name}"
            itemPictureMusicSinger.text = singerName
            if (item.mv == 0) {
                itemPictureMusicVideo.GONE()
            }
        }
    }
}