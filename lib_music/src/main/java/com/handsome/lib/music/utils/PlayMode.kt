package com.handsome.lib.music.utils

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/18
 * @Description:
 *
 */
enum class PlayMode {
    PLAY_MODE_LIST_LOOP,
    PLAY_MODE_SINGLE_CYCLE,
    PLAY_MODE_RANDOM
}

object PlayModeHelper {
    const val list_loop = "列表循环"
    const val single_cycle = "单曲循环"
    const val random = "随机播放"
}