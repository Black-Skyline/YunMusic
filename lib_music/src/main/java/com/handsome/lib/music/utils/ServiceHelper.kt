package com.handsome.lib.music.utils

import androidx.core.app.NotificationCompatExtras

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/18
 * @Description:
 *
 */
class ServiceHelper {
    companion object {
        // 字符串常量
        const val duration = "GET_CURRENT_AUDIO_DURATION"
        const val audioName = "GET_CURRENT_AUDIO_NAME"
        const val artistName = "GET_CURRENT_ARTIST_NAME"

        // Handler发送msg的what类型
        const val SENT_AUDIO_DURATION = 1
        const val SENT_AUDIO_NAME = 2
        const val SENT_ARTIST_NAME = 3


        // 通知栏操作行为常量
        const val PLAY = "play"  // 播放
        const val PAUSE = "pause"// 暂停
        const val PREV = "prev"  // 上一曲
        const val NEXT = "next"  // 下一曲
        const val CLOSE = "close"// 关闭
        const val channelID = "YunMusicService"  // 通知的渠道id
    }
}