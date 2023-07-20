package com.handsome.module.find.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.find.databinding.TopListRvPicItemBinding
import com.handsome.module.find.network.model.TopListData

class TopListPictureAdapter(private val onClick : (TopListData.Data) -> Unit) : ListAdapter<TopListData.Data,TopListPictureAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding: TopListRvPicItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            val viewGroup = binding.itemTopListPicImgBackground.parent as ViewGroup
            viewGroup.setOnClickListener { onClick(getItem(bindingAdapterPosition)) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(TopListRvPicItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            itemTopListPicImgBackground.setImageFromUrl(item.coverImgUrl)
            itemTopListPicTvName.text = item.name
        }
    }
}