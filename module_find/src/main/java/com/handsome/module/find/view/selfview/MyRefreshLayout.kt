package com.handsome.module.find.view.selfview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.abs

class MyRefreshLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs){

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var sign = false
        var startX= 0.0f
        var startY= 0.0f
        when(ev?.action){
            MotionEvent.ACTION_DOWN -> {
                sign = false
                startX = ev.x
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val currentX = ev.x
                val currentY = ev.y
                val dx = abs(currentX - startX)
                val dy = abs(currentY - startY)
                sign = dy  > dx + 30
            }
            MotionEvent.ACTION_UP -> {
                sign = false
            }
        }

        return sign
    }
}