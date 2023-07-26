package com.handsome.lib.music.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/23
 * @Description: 包装MusicService外部传来的音频播放信息
 *
 */
@Entity
data class WrapPlayInfo(
    val audioName: String,
    val artistName: String,
    val audioId: Long,
    val picUrl: String,
    var audioUrl: String? = null
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}
