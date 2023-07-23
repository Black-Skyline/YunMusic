package com.handsome.module.find.view.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
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
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.gsonSaveToSp
import com.handsome.lib.util.util.objectFromSp
import com.handsome.lib.util.util.shareText
import com.handsome.module.find.R
import com.handsome.module.find.databinding.ActivityRecommendDetailBinding
import com.handsome.module.find.network.exception.myCoroutineExceptionHandler
import com.handsome.module.find.network.model.RecommendDetailData
import com.handsome.module.find.view.adapter.RecommendDetailRvAdapter
import com.handsome.module.find.view.viewmodel.RecommendDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class RecommendDetailActivity : BaseActivity() {
    private val mBinding by lazy { ActivityRecommendDetailBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[RecommendDetailViewModel::class.java] }
    private val mRecommendDetailRvAdapter = RecommendDetailRvAdapter(::recommendDetailOnClick)
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
        initBar()
        initMusic()
        initClickPlay()
        initService()
    }

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

    private fun initMusic() {
        initMusicAdapter()
        initMusicCollect()
        getRecommendDetailData()
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

    private fun getRecommendDetailData() {
        mViewModel.getRecommendDetailData()
    }

    private fun recommendDetailOnClick(dailySong: RecommendDetailData.Data.DailySong) {
        dailySong.name.toast()
        //todo 播放逻辑！！！
    }

    private fun initMusicCollect() {
        fun doAfterGet(value : RecommendDetailData){
            mRecommendDetailRvAdapter.submitList(value.data.dailySongs)
            mBinding.apply {
                val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate.now().toString()
                } else {
                    "21/5"
                }
                recommendDetailTvTime.text = time
            }
        }
        lifecycleScope.launch(myCoroutineExceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.recommendDetailStateFlow.collectLatest {
                    if (it != null) {
                        if (it.code == 200){
                            doAfterGet(it)
                            gsonSaveToSp(it,"recommend_detail")
                        }else{
                            val value = objectFromSp<RecommendDetailData>("recommend_detail")
                            if (value!=null) doAfterGet(value)
                        }
                    }else{
                        val value = objectFromSp<RecommendDetailData>("recommend_detail")
                        if (value!=null) doAfterGet(value)
                    }
                }
            }
        }
    }

    private fun initMusicAdapter() {
        mBinding.recommendDetailRvMusic.apply {
            layoutManager =
                LinearLayoutManager(this@RecommendDetailActivity, RecyclerView.VERTICAL, false)
            adapter = mRecommendDetailRvAdapter
        }
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
                shareText("小帅哥快来玩啊: http://why.vin:2023/recommend/songs")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        fun startAction(context: Context) {
            context.startActivity(Intent(context, RecommendDetailActivity::class.java))
        }
    }
}