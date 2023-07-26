package com.handsome.lib.util.util

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator


/**
 * 实现无限旋转的效果
 * 返回一个可以操作的animator
 */

class MyRotationAnimate(val view: View){
    private val mAnimator: ValueAnimator = ValueAnimator.ofFloat(0f , 360f)

    init {
        // 设置动画重复次数为无限
        mAnimator.repeatCount = ObjectAnimator.INFINITE
        // 设置动画持续时间
        mAnimator.duration = 10000
        //设置平滑插值器
        mAnimator.interpolator = LinearInterpolator()
        mAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            view.rotation = value
            // 步骤5：刷新视图，即重新绘制，从而实现动画效果
            view.requestLayout();
        }
    }

    //启动动画，不间断启动
    fun startAnimate(){
        mAnimator.setFloatValues(view.rotation,view.rotation + 360f)
        mAnimator.start()
    }

    //暂停一个动画
    fun stopAnimate(){
        mAnimator.cancel()
    }

    fun setDuration(duration : Long){
        if (duration < 0) return
        mAnimator.duration = duration
    }

    fun setInterpolator(interpolator: Interpolator){
        mAnimator.interpolator = interpolator
    }

}

/**
translationX / translationY: 控制视图的水平和垂直平移。
rotation: 控制视图的旋转角度。
scaleX / scaleY: 控制视图的水平和垂直缩放。
alpha: 控制视图的透明度。
pivotX / pivotY: 控制视图的旋转和缩放的轴点（旋转和缩放的中心点）。
backgroundColor: 控制视图的背景颜色。
textColor: 控制文本视图的文本颜色。
height / width: 控制视图的高度和宽度。
scrollX / scrollY: 控制视图的滚动位置。
progress: 控制进度条的进度。
其他自定义属性: 您还可以使用属性动画来改变自定义视图或属性的值。
 */