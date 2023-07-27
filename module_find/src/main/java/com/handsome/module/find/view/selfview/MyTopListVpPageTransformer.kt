package com.handsome.module.find.view.selfview

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class MyTopListVpPageTransformer  : ViewPager2.PageTransformer{


    override fun transformPage(page: View, position: Float) {
        page.rotation = position * 30
    }
}