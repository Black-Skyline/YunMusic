package com.handsome.module.login.utils.topfuncation

import android.view.View


const val SINGLE_CLICK_RECORD = 0x001
const val DOUBLE_CLICK_RECORD = 0x001

/**
 * On single click listener
 *
 * @param Interval   时间间隔，要求后一次点击与前一次点击时间间隔小于该数值
 * @param clickEvent 传入的具体的点击事件
 * @receiver
 */
fun View.setOnSingleClickListener(Interval: Long = 1000, clickEvent: (View) -> Unit) {
    setOnClickListener {
        val tag = getTag(SINGLE_CLICK_RECORD) as? Long
        if (System.currentTimeMillis() - (tag ?: 0L)> Interval)
        {
            clickEvent(it)
        }
        it.setTag(SINGLE_CLICK_RECORD,System.currentTimeMillis())
    }
}


/**
 * Set on double click listener
 *
 * @param interval   时间间隔，要求两次连续点击时间间隔小于该数值
 * @param clickEvent 传入的具体的点击事件
 * @receiver
 */
fun View.setOnDoubleClickListener(interval: Long = 500, clickEvent: (View) -> Unit) {
    setOnClickListener {
        val tag = getTag(DOUBLE_CLICK_RECORD) as? Long
        if (System.currentTimeMillis() - (tag ?: 0L) < interval) {
            clickEvent(it)
        }
        it.setTag(DOUBLE_CLICK_RECORD, System.currentTimeMillis())
    }
}