package com.handsome.lib.util.extention

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.handsome.lib.util.BaseApp

/**
 * 已自带处于其他线程时自动切换至主线程发送
 */
fun toast(s: CharSequence?) {
    FreeChatToast.show(s, Toast.LENGTH_SHORT)
}

fun toastLong(s: CharSequence?) {
    FreeChatToast.show(s, Toast.LENGTH_LONG)
}

/**
 * 传入背景就能使用
 */
fun String.toast() = toast(this)
fun String.toastLong() = toastLong(this)

class FreeChatToast {
    companion object {

        /**
         * 已自带处于其他线程时自动切换至主线程发送
         */
        fun show(
            text: CharSequence?,
            duration: Int
        ) {
            if (text == null) return
            if (Thread.currentThread() !== Looper.getMainLooper().thread) {
                Handler(Looper.getMainLooper()).post { newInstance(text, duration).show() }
            } else {
                newInstance(text, duration).show()
            }
        }

        private fun newInstance(
            text: CharSequence,
            duration: Int
        ) : Toast {
            if (true) {
                val throwable = Throwable() // 获取堆栈信息
                val path = throwable.stackTrace
                    .toMutableList()
                    .apply { removeFirst() } // 从堆栈中去掉当前方法
                    .filter {
                        // 第一次先筛选出是自己代码中的方法栈
                        !it.isNativeMethod
                                && it.fileName != null
                                && it.fileName.endsWith(".kt")
                                && it.className.startsWith("com.")
                    }.after {
                        // 第二次筛选出不必要的方法栈
                        !it.className.contains(".base.") // 筛掉 base 包
                                && !it.fileName.startsWith("Base")
                                && !it.fileName.matches(Regex("[tT]oast")) // 去掉一些工具类
                    }.joinToString(separator = " <- ") {
                        "(${it.fileName}:${it.lineNumber})"
                    }
                Log.d("toast", "toast: text = $text   path: $path")
            }
            return Toast.makeText(BaseApp.mContext, text, duration)
        }

        /**
         * 寻找第一个满足条件后的子数组
         */
        private fun <T> List<T>.after(first:(T) -> Boolean): List<T> {
            val list = ArrayList<T>()
            var isFound = false
            forEach {
                if (isFound) {
                    list.add(it)
                } else {
                    isFound = first.invoke(it)
                }
            }
            return list
        }
    }
}