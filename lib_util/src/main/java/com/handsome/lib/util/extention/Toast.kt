package com.handsome.lib.util.extention

import android.os.Handler
import android.os.Looper
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
            return Toast.makeText(BaseApp.mContext, text, duration)
        }
    }
}