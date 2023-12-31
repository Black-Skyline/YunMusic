package com.handsome.module.mine



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.util.util.MyDIffUtil
import com.handsome.module.mine.databinding.ItemMusic1Binding

class LatestMusicAdapter(private val onClickMore:(wrapPlayInfo: WrapPlayInfo) -> Unit,private val onClick: (list: MutableList<WrapPlayInfo>, index: Int) -> Unit) :
    PagingDataAdapter<WrapPlayInfo, LatestMusicAdapter.MyHolder>(MyDIffUtil.getNewDiff()) {

    inner class MyHolder(val binding: ItemMusic1Binding) : RecyclerView.ViewHolder(binding.root) {
        init {
            //给more注册点击事件
            binding.itemMusicMore.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { it1 -> onClickMore(it1) }
            }
            //获取父布局，然后给父布局也就是整个item设置点击事件
            val viewGroup = binding.itemMusicNumber.parent as ViewGroup
            viewGroup.setOnClickListener {
                val list = ArrayList<WrapPlayInfo>()
                var wrapPlayInfo: WrapPlayInfo?
                //取点的item的前十个和后十个，每次点击每次刷新
                val beforeIndex =
                    if (bindingAdapterPosition - 10 >= 0) bindingAdapterPosition - 10 else 0
                val afterIndex =
                    if (bindingAdapterPosition + 10 <= itemCount) bindingAdapterPosition + 10 else itemCount
                for (i in beforeIndex until afterIndex) {
                    val song = getItem(i)
                    if (song!=null){
                        list.add(song)
                    }
                }
                onClick(list, bindingAdapterPosition - beforeIndex)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemMusic1Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        val number = (position + 1).toString()
        if (item != null) {
            holder.binding.apply {
                itemMusicNumber.text = number
                itemMusicName.text = item.audioName
                itemMusicSinger.text = item.artistName
            }
        }
    }
}