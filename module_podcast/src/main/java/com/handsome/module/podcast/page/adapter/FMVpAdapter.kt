package com.handsome.module.podcast.page.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.module.podcast.databinding.ItemPodcastFmVpContentBinding
import com.handsome.module.podcast.model.FMProgramsData

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */
class FMVpAdapter(private val onClick: (ProgramsList: List<FMProgramsData.Program>, index: Int) -> Unit) :
    RecyclerView.Adapter<FMVpAdapter.MyHolder>() {
    private var programsList: List<FMProgramsData.Program> = ArrayList()

    inner class MyHolder(val binding: ItemPodcastFmVpContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            if (programsList.isNotEmpty())
                binding.podcastItemFmContent.setOnClickListener {
                    onClick(programsList, bindingAdapterPosition % (programsList.size))
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemPodcastFmVpContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        if (programsList.isNotEmpty()) {  //将数据无限填充下去
            val item = programsList[position % (programsList.size)]
            holder.binding.podcastImgFmContentPic.setImageFromUrl(item.coverUrl)
            holder.binding.podcastTvFmContentTitle.text = item.description
            holder.binding.podcastTvFmContentSource.text = item.name
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(ProgramsList: List<FMProgramsData.Program>) {
        this.programsList = ProgramsList
        Log.d("LogicTest","进入了submitList")
        if (programsList.isNotEmpty()) Log.d("LogicTest","数据不为空")
        notifyDataSetChanged()
    }
}