package com.handsome.module.podcast.utils

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.handsome.module.podcast.R


val SINGLE_CLICK_RECORD = R.id.listener_single_click_is
val DOUBLE_CLICK_RECORD = R.id.listener_double_click_is

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
        if (System.currentTimeMillis() - (tag ?: 0L) > Interval) {
            clickEvent(it)
        }
        it.setTag(SINGLE_CLICK_RECORD, System.currentTimeMillis())
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


///**
// * 跳转fragment页面的封装写法
// *
// * @param T         目的地fragment的具体类型
// * @param start     调转的起始fragment
// * @param find      要寻找的fragment的tag
// * @param tag       标签，可置空
// * @param callback  目的地fragment的实例，这里用高阶函数传入，如：{ TargetFragment() }
// * @receiver
// */
//fun <T : Fragment> gotoFragmentPage(
//    start: Fragment, find: String, tag: String? = null, callback: () -> T
//) {
//    start.requireActivity().supportFragmentManager.apply {
//        val target = this.findFragmentByTag(find)
//        beginTransaction().apply {
//            hide(start)
//            if (target == null) {
//                add(R.id.?, callback(), tag)
//                Log.d("gotoFragmentPage","没找到")
//            } else {
//                show(target)
//                Log.d("gotoFragmentPage","找到了")
//            }
//            commit()
//        }
//    }
//}