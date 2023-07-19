package com.handsome.module.find.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.find.R
import com.handsome.module.find.databinding.FindRvBannerBelowItemBinding
import com.handsome.module.find.network.model.BannerBelowData

class FindBannerBelowRvAdapter(val onClick : (BannerBelowData.Data) -> Unit) : ListAdapter<BannerBelowData.Data,FindBannerBelowRvAdapter.MyHolder>(MyDIffUtil.getNewDiff()){

    inner class MyHolder(val binding : FindRvBannerBelowItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.findRvBannerBelowItemImg.setOnClickListener {
                onClick(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FindRvBannerBelowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.apply {
            val item = getItem(position)
            findRvBannerBelowItemImg.setImageFromUrl(item.iconUrl, placeholder = R.drawable.icon_qq)
            findRvBannerBelowItemTv.text = item.name
        }
    }

}


