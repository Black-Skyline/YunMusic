package com.handsome.lib.search.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.search.databinding.SearchSuggestionRvItemBinding
import com.handsome.lib.search.network.model.SearchSuggestionData
import com.handsome.lib.util.util.MyDIffUtil

class SearchSuggestionRvAdapter(val onSearchSuggestionClick: (SearchSuggestionData.Result.AllMatch) -> Unit) : ListAdapter<SearchSuggestionData.Result.AllMatch,SearchSuggestionRvAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding : SearchSuggestionRvItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.searchSuggestionRvItem.setOnClickListener{
                onSearchSuggestionClick(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(SearchSuggestionRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.searchSuggestionRvItem.text = getItem(position).keyword
    }
}