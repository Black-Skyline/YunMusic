package com.handsome.module.find.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.GONE
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.find.databinding.ItemPictureMusicBinding
import com.handsome.module.find.network.model.RecommendDetailData

class RecommendDetailRvAdapter(val onClick: (RecommendDetailData.Data.DailySong) -> Unit) :
    ListAdapter<RecommendDetailData.Data.DailySong, RecommendDetailRvAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding: ItemPictureMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                itemPictureMusicImg.setOnClickListener { onClick(getItem(bindingAdapterPosition)) }
                itemPictureMusicName.setOnClickListener { onClick(getItem(bindingAdapterPosition)) }
                itemPictureMusicSinger.setOnClickListener { onClick(getItem(bindingAdapterPosition)) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemPictureMusicBinding.inflate(
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