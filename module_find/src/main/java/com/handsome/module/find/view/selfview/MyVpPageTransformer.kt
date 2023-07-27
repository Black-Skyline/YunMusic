package com.handsome.module.find.view.selfview

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class MyVpPageTransformer : ViewPager2.PageTransformer {
    private val minScale = 0.85f  //最小缩放尺寸
    private val minAlpha = 0.5f   //最小透明度


    override fun transformPage(page: View, position: Float) {

        val scaleSize = minScale.coerceAtLeast(1 - abs(position))  //返回二者大的哪一个
        page.scaleX = scaleSize
        page.scaleY = scaleSize
        page.alpha = minAlpha.coerceAtLeast(1 - abs(position))   ///设置透明
    }
}