package com.handsome.module.login.utils

import com.handsome.lib.util.extention.toast
import kotlinx.coroutines.CoroutineExceptionHandler
import java.net.URLEncoder

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/19
 * @Description:
 *
 */
object URLUtil {
    /**
     * Exception handler
     *
     * 用于打印异常信息
     */
    val exceptionPrinter = CoroutineExceptionHandler{ _,e ->
        e.printStackTrace()
        e.toString().toast()
    }
    fun encodeStringByUTF8(raw:String) = URLEncoder.encode(raw, "UTF-8")
}