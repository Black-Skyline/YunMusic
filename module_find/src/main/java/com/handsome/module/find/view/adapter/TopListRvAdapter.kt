package com.handsome.module.find.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.find.databinding.TopListTvItemBinding
import com.handsome.module.find.network.model.TopListData

class TopListRvAdapter(private val onClick : (TopListData.Data,sharedView : View) -> Unit) : ListAdapter<TopListData.Data,TopListRvAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding : TopListTvItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            //todo 点击事件
            val viewGroup = binding.topListItemImg.rootView as ViewGroup
            viewGroup.setOnClickListener {
                onClick(getItem(bindingAdapterPosition),binding.topListItemImg)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(TopListTvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            topListItemImg.setImageFromUrl(item.coverImgUrl)
            topListItemTvName.text = item.name
            val first = "1 ${item.tracks[0].first}  ${item.tracks[0].second}"
            topListItemTvFirstSong.text = first
            val second = "2 ${item.tracks[1].first}  ${item.tracks[1].second}"
            topListItemTvSecondSong.text = second
            val third = "3 ${item.tracks[2].first}  ${item.tracks[2].second}"
            topListItemTvThirdSong.text = third
        }
    }

}