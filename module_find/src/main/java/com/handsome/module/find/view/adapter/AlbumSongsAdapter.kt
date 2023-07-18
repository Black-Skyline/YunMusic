package com.handsome.module.find.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.find.databinding.ItemMusicBinding
import com.handsome.module.find.network.model.AlbumData

class AlbumSongsAdapter : ListAdapter<AlbumData.Song, AlbumSongsAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding : ItemMusicBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            //todo 点击事件
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ItemMusicBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        val number =  (position+1).toString()
        holder.binding.apply {
            itemMusicNumber.text = number
            itemMusicName.text = item.name
            itemMusicSinger.text = item.ar[0].name
        }
    }

}