package com.handsome.module.podcast.utils

import android.util.Log
import com.handsome.lib.util.extention.toastLong
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */

/**
 * Exception handler
 *
 * 用于打印异常信息
 */
val exceptionPrinter = CoroutineExceptionHandler { _, e ->
    e.printStackTrace()
    e.toString().toastLong()
    Log.d("ExceptionHandler", "err:${e.toString()}")
}