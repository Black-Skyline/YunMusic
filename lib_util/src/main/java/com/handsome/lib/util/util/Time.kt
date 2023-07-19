package com.handsome.lib.util.util


/**
 * 获得当前时间戳
 */
fun getCurrentTime() : Long{
    val time = System.currentTimeMillis() / 100
    return time.toLong()
}