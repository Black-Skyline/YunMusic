package com.handsome.lib.music.page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.music.R
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.util.util.MyDIffUtil

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/25
 * @Description:
 *
 */
class PlaylistAdapter(
    private val data: MutableList<WrapPlayInfo>,
    private val itemOnClick: (item: View, position: Int) -> Unit,
) :
    ListAdapter<WrapPlayInfo, PlaylistAdapter.Holder>(MyDIffUtil.getNewDiff()) {

    inner class Holder(root: View) : RecyclerView.ViewHolder(root) {
        val audioName: TextView = root.findViewById(R.id.music_tv_playlist_content_audio_name)
        val artistName: TextView = root.findViewById(R.id.music_tv_playlist_content_artist_name)
        val removeItem: ImageView = root.findViewById(R.id.music_btn_playlist_content_audio_remove)

        init {
            removeItem.setOnClickListener {
                val position = bindingAdapterPosition
                data.remove(data[position])
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, data.size)
            }
            root.setOnClickListener {
                val position = bindingAdapterPosition
                itemOnClick(root, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music_play_list_content, parent, false)
        return Holder(view)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        val itemInfo = data[position]
        holder.apply {
            audioName.text = itemInfo.audioName
            artistName.text = "·${itemInfo.artistName}"
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}