package com.handsome.lib.mv.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.mv.databinding.MvRvItemRecommendBinding
import com.handsome.lib.mv.network.model.MvRecommendData
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil

class RecommendMvRvAdapter(val onRecommendClick : (MvRecommendData.Data) -> Unit) : ListAdapter<MvRecommendData.Data,RecommendMvRvAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding : MvRvItemRecommendBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            val viewGroup = binding.mvRvItemRecommendImg.parent as ViewGroup
            viewGroup.setOnClickListener {
                onRecommendClick(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MvRvItemRecommendBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.binding.apply {
                mvRvItemRecommendImg.setImageFromUrl(item.cover)
                mvRvItemRecommendTvSongName.text = item.name
                mvRvItemRecommendTvSingerName.text = item.artistName
            }
        }
    }
}