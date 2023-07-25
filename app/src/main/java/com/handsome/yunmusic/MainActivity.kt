package com.handsome.yunmusic

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.handsome.lib.music.MusicPlayActivity
import com.handsome.lib.music.sevice.MusicService
import com.handsome.lib.search.SearchActivity
//import com.handsome.lib.search.SearchActivity
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.extention.setImageFromLocalUri
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.getSharePreference
import com.handsome.module.find.view.fragment.FindFragment
import com.handsome.module.mine.MineFragment
//import com.handsome.module.find.view.fragment.FindFragment
import com.handsome.yunmusic.databinding.ActivityMainBinding
import java.io.File

class MainActivity : YunMusicActivity() {
    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var mImgPlay : ImageView

    // Service实例
    private lateinit var mMusicService: MusicService

    // Service是否已绑定
    private var mIsBound: Boolean = false

    //service的回调
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicPlayBinder
            mMusicService = binder.service
            mIsBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mIsBound = false
        }
    }

    //从相册中拿结果
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            findViewById<ImageView>(R.id.drawer_header_user_icon).setImageFromLocalUri(uri)
            val edit = getSharePreference("user_img").edit()
            edit.putString("user_img",uri.toString())
            edit.apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initBar()
        initVpAdapter()
        initNaviBottomClick()
        initDrawerNavi()
        initClickPlay()
        initService()
    }

    private fun initService() {
        Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    //播放逻辑
    private fun initClickPlay() {
        mImgPlay = findViewById<ImageView>(com.handsome.lib.util.R.id.main_bottom_music_image_play)
        //播放的点击事件，dj！
        mImgPlay.setOnClickListener {
            if (!mIsBound) {  //还未绑定service直接返回
                return@setOnClickListener
            }
            if (!mMusicService.isPrepared) {
                return@setOnClickListener
            }
            if (mMusicService.isPlaying()) {
                mImgPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_play)
                mMusicService.pausePlay()
            } else {
                mImgPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_stop)
                mMusicService.startPlay()
            }
        }
        val viewGroup = findViewById<ImageView>(com.handsome.lib.util.R.id.main_bottom_music_image_image).parent as ViewGroup
        viewGroup.setOnClickListener {
            startActivity(Intent(this,MusicPlayActivity::class.java)) //todo
        }
    }

    private fun initBar() {
        //点击事件
        mBinding.mainTopNavi.setOnClickListener {
            //打开侧边栏
            mBinding.mainDrawer.openDrawer(GravityCompat.START)
        }
        //获取搜索img所在的父布局，并且设置监听事件
        val viewGroup = mBinding.mainImgSearch.parent as ViewGroup
        viewGroup.setOnClickListener {
            SearchActivity.startAction(this)
        }
    }

    private fun initVpAdapter() {
        val fragmentVpAdapter = FragmentVpAdapter(this)
        //todo 等待加入的fragment
        fragmentVpAdapter.add(FindFragment::class.java).add(MineFragment::class.java)
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
        //增加侧滑监听，drawerlayout只有在显示的时候才会创建。
        mBinding.mainDrawer.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
                val imageUser = findViewById<ImageView>(R.id.drawer_header_user_icon)
                val img = getSharePreference("user_img").getString("user_img",null)
                if (img != null)  imageUser.setImageFromLocalUri(Uri.parse(img))
                imageUser.setOnClickListener {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
    }

    //每次重新恢复页面的时候也要进行判断播放状态
    override fun onStart() {
        super.onStart()
        if (mIsBound){
            //获取当前歌名字歌手名字
            if (mMusicService.isPlaying()) {
                mImgPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_stop)
            } else {
                mImgPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_play)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}