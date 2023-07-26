package com.handsome.lib.search.view.selfview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.handsome.lib.search.R


class MyFlowLayoutParams (c: Context, attrs: AttributeSet?) : ViewGroup.LayoutParams(c, attrs){
    var lineSpacing = 0
    var itemSpacing = 0

    init {
        val typedArray = c.obtainStyledAttributes(attrs, R.styleable.MyFlowLayout)
        lineSpacing = typedArray.getDimensionPixelSize(R.styleable.MyFlowLayout_FlowLayout_lineSpacing,0)
        itemSpacing = typedArray.getDimensionPixelSize(R.styleable.MyFlowLayout_FlowLayout_itemSpacing,0)
        typedArray.recycle()
    }
}