package com.handsome.lib.search.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.search.databinding.ItemPictureMusicBinding
import com.handsome.lib.search.network.model.SearchResultData
import com.handsome.lib.util.extention.GONE
import com.handsome.lib.util.util.MyDIffUtil

class SearchResultRvAdapter(val onClick: (SearchResultData.Result.Song) -> Unit) :
    PagingDataAdapter<SearchResultData.Result.Song, SearchResultRvAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding: ItemPictureMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                val viewGroup = itemPictureMusicName.parent as ViewGroup
                viewGroup.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { it1 -> onClick(it1) }
                }
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
        if (item!=null){
            holder.binding.apply {
                val number = (position+1).toString()
                itemPictureMusicTvNumber.text = number
                itemPictureMusicName.text = item.name
                val singerName =" ${item.album.name} ${item.artists[0].name}"
                itemPictureMusicSinger.text = singerName
                if (item.mvid == 0) {
                    itemPictureMusicVideo.GONE()
                }
            }
        }
    }

}