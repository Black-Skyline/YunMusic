package com.handsome.lib.music.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/23
 * @Description: 包装MusicService外部传来的音频播放信息
 *
 */
data class WrapPlayInfo(
    val audioName: String,
    val artistName: String,
    val audioId: Long,
    val picUrl: String,
    var audioUrl: String? = null
) : Serializable
//data class WrapPlayInfo(
//    val audioName: String,
//    val artistName: String,
//    val audioId: Long,
//    val picUrl: String,
//    var audioUrl: String? = null
//) : Parcelable {
//    companion object CREATOR : Parcelable.Creator<WrapPlayInfo> {
//        override fun createFromParcel(parcel: Parcel): WrapPlayInfo {
//            return WrapPlayInfo(parcel)
//        }
//
//        override fun newArray(size: Int): Array<WrapPlayInfo?> {
//            return arrayOfNulls(size)
//        }
//
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(audioName)
//        parcel.writeString(artistName)
//        parcel.writeLong(audioId)
//        parcel.writeString(picUrl)
//        parcel.writeString(audioUrl)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    constructor(parcel: Parcel) : this(
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.readLong(),
//        parcel.readString()!!,
//        parcel.readString(),
//    )
//}
