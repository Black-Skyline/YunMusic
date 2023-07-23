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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.music.MusicPlayActivity
import com.handsome.lib.music.sevice.MusicService
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.shareText
import com.handsome.module.find.R
import com.handsome.module.find.databinding.ActivitySpecialEditionBinding
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
    private val mAlbumSongsAdapter = AlbumSongsAdapter()

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
            if (mMusicService.isPlaying()) {
                mImgPlay.setImageResource(com.handsome.lib.util.R.drawable.icon_bottom_music_stop)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mIsBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val id = intent.getLongExtra("id",32311)  //网上随便找的专辑id
        initBar()
        initRv(id)
        initClickPlay()
        initService()
    }

    private fun initRv(id : Long) {
        initMusicAdapter()
        initMusicCollect()
        getMusicData(id)
    }

    //每次重新恢复页面的时候也要进行判断播放状态
    override fun onStart() {
        super.onStart()
        if (mIsBound){
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
            startActivity(Intent(this, MusicPlayActivity::class.java)) //todo
        }
    }

    private fun initMusicAdapter() {
        mBinding.specialEditionRvMusic.apply {
            layoutManager = LinearLayoutManager(this@SpecialEditionActivity,RecyclerView.VERTICAL,false)
            adapter = mAlbumSongsAdapter
        }
    }

    private fun getMusicData(id: Long) {
        mViewModel.getAlbumData(id)
    }

    private fun initMusicCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewModel.stateFlow.collectLatest {
                    if (it != null){
                        mAlbumSongsAdapter.submitList(it.songs)
                        mBinding.apply {
                            val album = it.album
                            val singer = "歌手:${album.artists[0].name} >"
                            val publishTime = "发行时间 : ${SimpleDateFormat("yyyy.MM.dd").format(Date(album.publishTime))}"
                            val playAll =  "播放全部(${it.songs.size})"
                            specialEditionCollapsingImg.setImageFromUrl(album.blurPicUrl, placeholder = R.drawable.icon_picture)
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

    private fun initBar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_more,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { finish()
                return true
            }
            R.id.menu_item_music_more -> {
                shareText("小帅哥快来玩啊: http://why.vin:2023/album?id=32311")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        fun startAction(context : Context, id : Long){
            val intent = Intent(context,SpecialEditionActivity::class.java)
            intent.putExtra("id",id)
            context.startActivity(intent)
        }
    }
}