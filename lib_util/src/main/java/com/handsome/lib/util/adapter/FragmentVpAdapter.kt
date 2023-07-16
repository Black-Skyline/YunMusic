package com.handsome.lib.util.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


/**
//1，2是两个构造器，第一个构造器用于在activity中使用viewPager2，传入this即可
第二个构造器用于在fragment使用Viewpager2，传入this应该也就可以了
都会获得fragment的manager和lifecycler，提供了一种更加简便的创建方式
//3 这个是一个lambda表达式的list，并没有创建fragment，维护的是待创建的fragment对象
//4传入一段lambda表达式，然后储存到list中
//5 传入fragment的类::，这个要注意fragment中要有newInstance静态方法
//6 开始创建fragment，调用list指定位置的fragmet的lambda表达式的invoke方法，也就会调用lambda表达式创建fragment，也就是获得Fragment实例
 */

class FragmentVpAdapter private constructor(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    //1
    constructor(activity: FragmentActivity) : this(activity.supportFragmentManager, activity.lifecycle)
    //2
    constructor(fragment: Fragment) : this(fragment.childFragmentManager, fragment.lifecycle)

    //3
    private val mFragments = arrayListOf<() -> Fragment>()

    //4
    fun add(fragment: () -> Fragment): FragmentVpAdapter {
        mFragments.add(fragment)
        return this
    }

    //5
    fun add(fragment: Class<out Fragment>): FragmentVpAdapter {
        // 官方源码中在恢复 Fragment 时就是调用的这个反射方法，该方法应该不是很耗性能 :)
        mFragments.add { fragment.newInstance() }
        return this
    }

    override fun getItemCount(): Int = mFragments.size

    //6
    override fun createFragment(position: Int): Fragment = mFragments[position].invoke()
}