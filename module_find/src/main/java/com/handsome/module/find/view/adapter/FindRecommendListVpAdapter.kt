package com.handsome.module.find.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.find.R
import com.handsome.module.find.databinding.FindRvRecommendListItemBinding
import com.handsome.module.find.network.model.RecommendMusicListData

class FindRecommendListVpAdapter(val onClick : (RecommendMusicListData.Result,sharedView : View) -> Unit) : ListAdapter<RecommendMusicListData.Result,FindRecommendListVpAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding : FindRvRecommendListItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.findVpRecommendListItemImg.setOnClickListener {
                onClick(getItem(bindingAdapterPosition),binding.findVpRecommendListItemImg)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FindRvRecommendListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            findVpRecommendListItemImg.setImageFromUrl(item.picUrl, placeholder = R.drawable.icon_big_big_picture)
            val playNumber = "${item.playCount / 10000}万"
            findVpRecommendListItemNumber.text = playNumber
            findVpRecommendListItemTvDescribe.text = item.name
        }
    }
}