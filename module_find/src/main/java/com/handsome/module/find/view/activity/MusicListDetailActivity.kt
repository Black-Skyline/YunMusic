package com.handsome.module.find.view.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.music.page.view.MusicPlayActivity
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.music.sevice.MusicService
import com.handsome.lib.mv.view.MvActivity
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.util.MyRotationAnimate
import com.handsome.lib.util.util.shareText
import com.handsome.module.find.R
import com.handsome.module.find.databinding.ActivityMusicListDetailBinding
import com.handsome.lib.mv.network.exception.myCoroutineExceptionHandler
import com.handsome.module.find.network.model.MusicListDetailData
import com.handsome.module.find.view.adapter.MusicListDetailAdapter
import com.handsome.module.find.view.viewmodel.MusicListDetailViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MusicListDetailActivity : BaseActivity() {
    private val mBinding by lazy { ActivityMusicListDetailBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[MusicListDetailViewModel::class.java] }
    private val mMusicListDetailAdapter = MusicListDetailAdapter(::onClickMv,::onClickMore,::onClickMusicListDetail)  //歌单详情也可以用


    private lateinit var mImgPlay: ImageView
    private lateinit var mImgAlbum : ImageView   //下面的唱片view
    private lateinit var mAnimator : MyRotationAnimate  //下面的唱片的旋转动画

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
            getBottomInfo()   //得到底部播放栏信息得
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mIsBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val id = intent.getLongExtra("id", 24381616)  //网上随便找的歌单id
        initView()
        initBar()
        initRv(id)
        initTop(id)
        initClickPlay()  //和播放相关的两个方法
        initService()
        initClickPlayAll()   //播放全部的点击事件,后面一次会覆盖前面一次
    }

    private fun initView() {
        mImgPlay = findViewById(com.handsome.lib.util.R.id.main_bottom_music_image_play)
        mImgAlbum = findViewById(com.handsome.lib.util.R.id.main_bottom_music_image_image)
        mAnimator = MyRotationAnimate(mImgAlbum)
    }

    //每次重新恢复页面的时候也要进行判断播放状态
    override fun onStart() {
        super.onStart()
        if (mIsBound) {
            getBottomInfo()
        }
    }

    //加载底部信息
    private fun getBottomInfo() {
        //获取当前歌名字歌手名字
        val songName = mMusicService.getCurAudioName()
        val singerName = mMusicService.getCurArtistName()
        val picUrl = mMusicService.getCurAudioPicUrl() //可能为找不到
        findViewById<TextView>(com.handsome.lib.util.R.id.main_bottom_music_tv_name).text = songName
        findViewById<TextView>(com.handsome.lib.util.R.id.main_bottom_music_tv_singer).text =
            singerName
        if (picUrl != "找不到") {
            findViewById<ImageView>(com.handsome.lib.util.R.id.main_bottom_music_image_image).setImageFromUrl(
                picUrl
            )
        }
        if (mMusicService.isPlaying()) {
            mImgPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_stop)
            mAnimator.startAnimate()
        } else {
            mImgPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_play)
            mAnimator.stopAnimate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    private fun initTop(id: Long) {
        initTopCollect()
        getTopInfo(id)
    }

    private fun initService() {
        Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    //播放逻辑
    private fun initClickPlay() {
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
                mAnimator.stopAnimate()
            } else {
                mImgPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_stop)
                mMusicService.startPlay()
                mAnimator.startAnimate()
            }
        }
        val viewGroup = mImgAlbum.parent as ViewGroup
        viewGroup.setOnClickListener {
            startActivity(Intent(this, MusicPlayActivity::class.java))
        }
    }

    private fun getTopInfo(id: Long) {
        mViewModel.getSingleMusicDetailData(id)
    }

    override fun onStop() {
        super.onStop()
        mAnimator.stopAnimate()
    }

    private fun initTopCollect() {
        lifecycleScope.launch(myCoroutineExceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.singleMusicListDetailStateFlow.collectLatest {
                    if (it != null && it.code == 200) {
                        val detail = it.playlist
                        mBinding.apply {
                            musicListCollapsingImg.setImageFromUrl(
                                detail.coverImgUrl, placeholder = R.drawable.icon_picture
                            )
                            musicListCollapsingTvColumnName.text = detail.name
                            musicListCollapsingTvColumnSinger.text = detail.creator.nickname
                            musicListCollapsingTvColumnDescribe.text = detail.description
                            musicListTvPlayAll.text = "播放全部"
                        }
                    }
                }
            }
        }
    }


    //播放全部的点击事件
    private fun initClickPlayAll() {
        mBinding.musicListTvPlayAll.setOnClickListener {
            //点击第一条数据
            val firstView = mBinding.musicListRvMusic.layoutManager?.findViewByPosition(0)
            firstView?.performClick()
        }
    }

    private fun initRv(id: Long) {
        initMusicAdapter()
        initMusicCollect(id)
    }

    private fun initMusicAdapter() {
        mBinding.musicListRvMusic.apply {
            layoutManager = LinearLayoutManager(
                this@MusicListDetailActivity,
                RecyclerView.VERTICAL, false
            )
            adapter = mMusicListDetailAdapter
        }
    }

    private fun initMusicCollect(id: Long) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.getMusicListData(id).catch {
                    it.printStackTrace()
                }.collect {
                    mMusicListDetailAdapter.submitData(it)
                }
            }
        }
    }

    private fun initBar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onClickMore(song: MusicListDetailData.Song) {
        val shareText = "${song.al.name.trim()}的${song.name.trim()}特别好听，你也来听听吧！"
        shareText(shareText)
    }

    private fun onClickMv(data :MusicListDetailData.Song) {
        if (mIsBound){
            mMusicService.pausePlay()
        }
        MvActivity.startAction(this,data.mv,data.name,data.ar[0].name,data.al.picUrl)
    }
    private fun onClickMusicListDetail(list: MutableList<WrapPlayInfo>, index: Int) {
        MusicPlayActivity.startWithPlayList(this, list, index)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_more, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.menu_item_music_more -> {
                shareText("小帅哥快来玩啊: http://why.vin:2023/playlist/track/all?id=24381616")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun startAction(context: Activity, id: Long, sharedView: View) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                context,
                sharedView,
                "music_detail_list"
            )
            val intent = Intent(context, MusicListDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent, options.toBundle())
        }
    }
}