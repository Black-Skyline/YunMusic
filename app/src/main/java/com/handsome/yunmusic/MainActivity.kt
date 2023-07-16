package com.handsome.yunmusic

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.extention.setImageFromId
import com.handsome.lib.util.extention.toast
import com.handsome.yunmusic.databinding.ActivityMainBinding

class MainActivity : YunMusicActivity(){
    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initBar()
        initVpAdapter()
        initNaviBottomClick()
        initDrawerNavi()
    }

    private fun initBar() {
        setSupportActionBar(mBinding.mainTb)
        supportActionBar?.let {
            title = ""
            //下面的方法是让导航按钮显示出来，原来是返回的箭头
            it.setDisplayHomeAsUpEnabled(true)
            //下面是更改导航的图标
            it.setHomeAsUpIndicator(R.drawable.icon_bottom_music_list)
        }
    }

    private fun initVpAdapter() {
        val fragmentVpAdapter = FragmentVpAdapter(this)
        //todo 等待加入的fragment
//        fragmentVpAdapter
        mBinding.mainNaviVp.adapter = fragmentVpAdapter
        mBinding.mainNaviVp.isUserInputEnabled = false;  //禁止vp滑动的方法,会让banner不管用
    }

    private fun initNaviBottomClick() {
        mBinding.mainNaviBottom.setOnItemSelectedListener {
            when (it.itemId) {
                //todo 等待加入fragment之后增添索引！
                R.id.menu_navi_bottom_find -> {
                    //第二个参数是设置是否过度动画
                    mBinding.mainNaviVp.setCurrentItem(0, false)
                    "第一个".toast()
                }

                R.id.menu_navi_bottom_radio -> {
                    mBinding.mainNaviVp.setCurrentItem(1, false)
                    "第er个".toast()
                }

                R.id.menu_navi_bottom_music -> {
                    mBinding.mainNaviVp.setCurrentItem(2, false)
                    "第三三三个".toast()
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun initDrawerNavi() {
        mBinding.mainDrawerNavigation.apply {
            //这个是用来设置默认选中的
            setCheckedItem(R.id.item_drawer_setting)
            //这个是用来设置监听点击事件的，并且只能点击下面的菜单
            setNavigationItemSelectedListener {
                //todo 等待设置点击事件
                when (it.itemId) {
                    R.id.item_drawer_setting -> {
                        Log.d("TAG", "initDrawerNavi: 1")
                    }

                    R.id.item_drawer_about -> {
                        Log.d("TAG", "initDrawerNavi: 2")
                    }

                    R.id.item_drawer_exit -> {
                        Log.d("TAG", "initDrawerNavi: 3")
                    }
                }
                return@setNavigationItemSelectedListener true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("lx", "我被点击了: ")
        when(item.itemId){
            //home按钮的id永远是android.R.id.home，
            //这一行是menu的点击事件
            android.R.id.home -> {
                mBinding.mainDrawer.openDrawer(GravityCompat.START)
                return true}
        }
        return super.onOptionsItemSelected(item)
    }
}