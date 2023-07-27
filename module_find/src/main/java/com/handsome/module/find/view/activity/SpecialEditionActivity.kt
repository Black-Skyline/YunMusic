package com.handsome.module.find.view.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
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
import com.handsome.module.find.databinding.ActivitySpecialEditionBinding
import com.handsome.module.find.network.model.AlbumData
import com.handsome.module.find.view.adapter.AlbumSongsAdapter
import com.handsome.module.find.view.viewmodel.SpecialEditionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

//也是album，网上搜的时候有点不靠谱。。
class SpecialEditionActivity : BaseActivity() {
    private val mBinding by lazy { ActivitySpecialEditionBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[SpecialEditionViewModel::class.java] }
    private val mAlbumSongsAdapter = AlbumSongsAdapter(::onClickMv,::onClickMore,::onClickAlbum)

    private lateinit var mImgPlay : ImageView   //播放按键view
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
            getBottomInfo()  //连接成功加载底部信息
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mIsBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initView()
        val id = intent.getLongExtra("id", 32311)  //网上随便找的专辑id
        initBar()
        initRv(id)
        initClickPlay()
        initService()
        initClickPlayAll()
    }

    private fun initView() {
        mImgPlay = findViewById(com.handsome.lib.util.R.id.main_bottom_music_image_play)
        mImgAlbum = findViewById(com.handsome.lib.util.R.id.main_bottom_music_image_image)
        mAnimator = MyRotationAnimate(mImgAlbum)
    }

    private fun initRv(id: Long) {
        initMusicAdapter()
        initMusicCollect()
        getMusicData(id)
    }

    //每次重新恢复页面的时候也要进行判断播放状态
    override fun onStart() {
        super.onStart()
        if (mIsBound) {
            getBottomInfo()
        }
    }

    private fun initClickPlayAll() {
        mBinding.specialEditionRvMusic.setOnClickListener {
            //代码模拟点击第一条数据
            val firstView = mBinding.specialEditionRvMusic.layoutManager?.findViewByPosition(0)
            firstView?.performClick()
        }
    }

    private fun getBottomInfo() {
        //获取当前歌名字歌手名字
        val songName = mMusicService.getCurAudioName()
        val singerName = mMusicService.getCurArtistName()
        val picUrl = mMusicService.getCurAudioPicUrl() //可能为找不到
        findViewById<TextView>(com.handsome.lib.util.R.id.main_bottom_music_tv_name).text = songName
        findViewById<TextView>(com.handsome.lib.util.R.id.main_bottom_music_tv_singer).text = singerName
        if (picUrl != "找不到") {
            mImgAlbum.setImageFromUrl(picUrl)
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

    override fun onStop() {
        super.onStop()
        mAnimator.stopAnimate()
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
        val viewGroup = mImgPlay.parent as ViewGroup
        viewGroup.setOnClickListener {
            startActivity(Intent(this, MusicPlayActivity::class.java)) //todo
        }
    }

    private fun initMusicAdapter() {
        mBinding.specialEditionRvMusic.apply {
            layoutManager =
                LinearLayoutManager(this@SpecialEditionActivity, RecyclerView.VERTICAL, false)
            adapter = mAlbumSongsAdapter
        }
    }

    private fun getMusicData(id: Long) {
        mViewModel.getAlbumData(id)
    }

    private fun initMusicCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.stateFlow.collectLatest {
                    if (it != null) {
                        mAlbumSongsAdapter.submitList(it.songs)
                        mBinding.apply {
                            val album = it.album
                            val singer = "歌手:${album.artists[0].name} >"
                            val publishTime =
                                "发行时间 : ${SimpleDateFormat("yyyy.MM.dd").format(Date(album.publishTime))}"
                            val playAll = "播放全部(${it.songs.size})"
                            specialEditionCollapsingImg.setImageFromUrl(
                                album.blurPicUrl,
                                placeholder = R.drawable.icon_picture
                            )
                            specialEditionCollapsingTvColumnName.text = album.name
                            specialEditionCollapsingTvColumnSinger.text = singer
                            specialEditionCollapsingTvColumnTime.text = publishTime
                            specialEditionCollapsingTvColumnDescribe.text = album.description.trim()
                            specialEditionTvPlayAll.text = playAll
                        }
                    }
                }
            }
        }
    }

    //初始化
    private fun onClickAlbum(list: MutableList<WrapPlayInfo>, index: Int) {
        MusicPlayActivity.startWithPlayList(this, list, index)
    }

    private fun onClickMore(song: AlbumData.Song) {
        val shareText = "${song.al.name.trim()}的${song.name.trim()}特别好听，你也来听听吧！"
        shareText(shareText)
    }

    private fun onClickMv(data : AlbumData.Song) {
        mMusicService.pausePlay()
        MvActivity.startAction(this,data.mv,data.name,data.al.name,data.al.picUrl)
    }


    private fun initBar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                shareText("小帅哥快来玩啊: http://why.vin:2023/album?id=32311")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun startAction(context: Context, id: Long) {
            val intent = Intent(context, SpecialEditionActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}