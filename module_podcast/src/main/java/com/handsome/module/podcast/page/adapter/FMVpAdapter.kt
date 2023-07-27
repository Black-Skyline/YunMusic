package com.handsome.module.podcast.page.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.module.podcast.databinding.ItemPodcastNormalRecommendContentBinding
import com.handsome.module.podcast.model.FMProgramsData

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */
class FMVpAdapter(private val onClick : (ProgramsList : List<FMProgramsData.Program>, index : Int) -> Unit) : RecyclerView.Adapter<FMVpAdapter.MyHolder>() {
    private var ProgramsList : List<FMProgramsData.Program> = ArrayList()

    inner class MyHolder(val binding : ItemPodcastNormalRecommendContentBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.podcastImgNormalRecommendItemBackground .setOnClickListener {
                onClick(ProgramsList,bindingAdapterPosition%(ProgramsList.size))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ItemPodcastNormalRecommendContentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        if (ProgramsList.isNotEmpty()){  //将数据无限填充下去
            holder.binding.podcastImgNormalRecommendItemBackground.setImageFromUrl(ProgramsList[position%ProgramsList.size].coverUrl)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(ProgramsList: List<FMProgramsData.Program>){
        this.ProgramsList = ProgramsList
        notifyDataSetChanged()
    }
}