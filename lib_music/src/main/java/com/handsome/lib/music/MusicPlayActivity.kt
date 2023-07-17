package com.handsome.lib.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.viewModels
import com.handsome.lib.music.sevice.MusicService
import com.handsome.lib.music.viewmodel.MusicPlayViewModel

class MusicPlayActivity : AppCompatActivity() {
    private val model by viewModels<MusicPlayViewModel>()
    private lateinit var serviceOperator: MusicService

    // service是否已绑定的标志位
    private var isBinding: Boolean = false

    /**
     * 连接器Connection，负责Activity与Service的通信
     */
    private val connection = object : ServiceConnection {
        // 服务已创建完成，通信连接已建立完成，Service放入服务的具体操作者binder，然后回调该方法
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isBinding = true
            serviceOperator = (service as MusicService.MusicPlayBinder).service
        }

        // 通信连接断开，回调该方法
        override fun onServiceDisconnected(name: ComponentName?) {
            isBinding = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_play)

        initView()
        initClick()
        initEvent()
        initObserve()
        startMusicPlayService()
    }

    /**
     * Activity与MusicService解绑
     */
    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        isBinding = false
    }

    /**
     * 开启MusicPlayService
     */
    private fun startMusicPlayService() {
        Intent(this, MusicService::class.java).also {
            bindService(it, connection, BIND_AUTO_CREATE)
        }
    }

    private fun initObserve() {

    }

    private fun initEvent() {

    }

    private fun initView() {
    }

    private fun initClick() {
        findViewById<View>(R.id.bt_play_mode)
        findViewById<View>(R.id.bt_previous_song).setOnClickListener {
            serviceOperator.previousSong()
        }
        findViewById<View>(R.id.bt_play_select).setOnClickListener {
            if (serviceOperator.isPrepare) {
                serviceOperator.isPlaying().also {
                    model.setPlayingState(it)
                    if (it) {
                        // 当前音乐正在播放，转为暂停状态
                        serviceOperator.pausePlay()
                    } else {
                        // 当前音乐正暂停，转为播放状态
                        serviceOperator.startPlay()
                    }
                }
            } else {
                // MediaPlay还未准备好
                serviceOperator.updateCurSong(0)
            }
        }
        findViewById<View>(R.id.bt_next_song).setOnClickListener {
            serviceOperator.nextSong()
        }
        findViewById<View>(R.id.bt_playlist)
        findViewById<View>(R.id.iv_music_pic)
    }
}