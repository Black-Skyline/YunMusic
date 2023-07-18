package com.handsome.lib.music

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.handsome.lib.music.sevice.MusicService
import com.handsome.lib.music.utils.MillisToTimeFormat
import com.handsome.lib.music.utils.PlayMode
import com.handsome.lib.music.utils.PlayModeHelper
import com.handsome.lib.music.utils.ServiceHelper
import com.handsome.lib.music.viewmodel.MusicPlayViewModel

class MusicPlayActivity : AppCompatActivity() {
    private val model by viewModels<MusicPlayViewModel>()
    private lateinit var serviceOperator: MusicService

    // service是否已绑定的标志位
    private var isBinding: Boolean = false

    // 状态位，进度条是否正在被用户拖拽
    private var isSeekbarDragging = false

    /**
     * 连接器Connection，负责Activity与Service的通信
     */
    private val connection = object : ServiceConnection {
        // 服务已创建完成，通信连接已建立完成，Service放入服务的具体操作者binder，然后回调该方法
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isBinding = true
            serviceOperator = (service as MusicService.MusicPlayBinder).service
            serviceOperator.updateCurSong(0)
            serviceOperator.setPlayMode(model.playMode.value!!)
            /**
             * 不断的获取播放器的进度，将其同步到ViewModel中
             */
            model.setCurProgress { serviceOperator.getCurProgress() }
            /**
             * 添加回调任务：播放器准备完成后传递duration值给model
             */
            serviceOperator.addCallbackToTasks(ServiceHelper.duration) {
                model.setSongDuration(it as Int)
            }
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
        model.isPlaying.observe(this) {
            findViewById<ImageView>(R.id.bt_play_select).apply {
                if (it)
                    setImageResource(R.drawable.ic_media_click_pause_50)
                else
                    setImageResource(R.drawable.ic_media_click_play_50)
            }
        }
        model.duration.observe(this) {
            findViewById<TextView>(R.id.tv_all_time).text =
                MillisToTimeFormat.toMinutesAndSeconds(it)
            findViewById<SeekBar>(R.id.play_progress).max = it
        }
        model.curProgress.observe(this) {
            findViewById<TextView>(R.id.tv_cur_time).text =
                MillisToTimeFormat.toMinutesAndSeconds(it)

            findViewById<SeekBar>(R.id.play_progress).progress = it
        }
        model.playMode.observe(this) {
            findViewById<ImageView>(R.id.bt_play_mode).apply {
                when (it) {
                    PlayMode.PLAY_MODE_SINGLE_CYCLE -> {
                        setImageResource(R.drawable.ic_media_playmode_single_cycle_24)
                        Toast.makeText(
                            this@MusicPlayActivity,
                            PlayModeHelper.single_cycle,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    PlayMode.PLAY_MODE_LIST_LOOP -> {
                        setImageResource(R.drawable.ic_media_playmode_list_loop_24)
                        Toast.makeText(
                            this@MusicPlayActivity,
                            PlayModeHelper.list_loop,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    PlayMode.PLAY_MODE_RANDOM -> {
                        setImageResource(R.drawable.ic_media_playmode_single_cycle_24)
                        Toast.makeText(
                            this@MusicPlayActivity,
                            PlayModeHelper.random,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        // it is null
                    }
                }
            }
        }
    }

    private fun initEvent() {

    }

    private fun initView() {
    }

    private fun initClick() {
        // 监听底部的项
        findViewById<View>(R.id.bt_play_mode).setOnClickListener {
            model.changePlayMode()
            serviceOperator.setPlayMode(model.playMode.value!!)
        }
        findViewById<View>(R.id.bt_previous_song).setOnClickListener {
            serviceOperator.previousSong()
            serviceOperator.awaitResult(5, model.isPlaying.value!!)
        }
        findViewById<View>(R.id.bt_play_select).setOnClickListener {
            if (serviceOperator.isPrepared) {
                serviceOperator.isPlaying().also {
                    model.setPlayingState(!it)
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
            serviceOperator.awaitResult(5, model.isPlaying.value!!)
        }
        findViewById<View>(R.id.bt_playlist)
        findViewById<View>(R.id.iv_music_pic)

        // 监听进度条
        findViewById<SeekBar>(R.id.play_progress).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(it: SeekBar?, progress: Int, b: Boolean) {
                // 用户拖动产生的进度更新，此时ViewModel里的进度数据还未更新
                findViewById<SeekBar>(R.id.play_progress).progress = progress
                findViewById<TextView>(R.id.tv_cur_time).text =
                    MillisToTimeFormat.toMinutesAndSeconds(progress)
            }

            override fun onStartTrackingTouch(it: SeekBar?) {
                isSeekbarDragging = true
                model.setSeekbarDragState(true)
            }

            override fun onStopTrackingTouch(it: SeekBar?) {
                // 让播放器更新进度
                serviceOperator.seekToPosition(it!!.progress)
                model.setSeekbarDragState(false)
                isSeekbarDragging = false
            }
        })
    }

}



