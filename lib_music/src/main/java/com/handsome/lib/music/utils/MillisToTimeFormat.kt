package com.handsome.lib.music.utils


/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/18
 * @Description:
 *
 */
object MillisToTimeFormat {
    fun toMinutesAndSeconds(millis: Int): String {
        val allSeconds = millis / 1000
        val minutes = allSeconds / 60
        val lastSeconds = allSeconds % 60
        return String.format("%02d:%02d", minutes, lastSeconds)
    }

}