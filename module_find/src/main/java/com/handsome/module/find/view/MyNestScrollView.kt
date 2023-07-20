package com.handsome.module.find.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class MyNestScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return when(ev?.action){
            MotionEvent.ACTION_MOVE -> true
            else -> super.dispatchTouchEvent(ev)
        }
    }
}