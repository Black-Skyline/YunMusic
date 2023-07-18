package com.handsome.module.find.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.module.find.databinding.FindVpBannerItemBinding
import com.handsome.module.find.network.model.BannerData

class FindBannerVpAdapter(private val onClick : (bannerData : BannerData.Banner) -> Unit) : RecyclerView.Adapter<FindBannerVpAdapter.MyHolder>() {
    private var findBannerList : List<BannerData.Banner> = ArrayList()

    inner class MyHolder(val binding : FindVpBannerItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.findVpBannerImg.setOnClickListener {
                onClick(findBannerList[(bindingAdapterPosition%(findBannerList.size))])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FindVpBannerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        if (findBannerList.isNotEmpty()){  //将数据无限填充下去
            holder.binding.findVpBannerImg.setImageFromUrl(findBannerList[position%findBannerList.size].pic)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(findBannerList: List<BannerData.Banner>){
        this.findBannerList = findBannerList
        notifyDataSetChanged()
    }
}