package com.handsome.lib.music.utils

import java.util.Random


/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/23
 * @Description:
 *
 */
object RandomNumber {
    fun getNumberExcludeAppoint(range: Int, exclude: Int? = null): Int {
        val random = Random()
        var number: Int
        while (true) {
            number = random.nextInt(range)
            if (exclude == null || exclude != number)
                break
        }
        return number
    }
}