package com.handsome.module.podcast.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/28
 * @Description:
 *
 */
class MultiplePagesTransformer : ViewPager2.PageTransformer {
    private val minScale = 0f  //最小缩放尺寸
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        if (position < -1f) {
            page.alpha = 0f
        } else if (position <= 0) {
            page.alpha = 1f
            page.translationX = 0f
            page.scaleX = 1f
            page.scaleY = 1f
        } else if (position <= 1f) {
            page.alpha = 1f
            page.translationX = -pageWidth * position
            val scaleFactor: Float = minScale + (1 - minScale) * (1 - abs(position))
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        } else {
            page.alpha = 0f
        }
    }

}