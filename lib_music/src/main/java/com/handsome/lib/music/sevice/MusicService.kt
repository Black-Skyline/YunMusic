package com.handsome.lib.music.sevice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.handsome.lib.music.R
import com.handsome.lib.music.utils.PlayMode
import com.handsome.lib.music.utils.ServiceHelper
import java.io.IOException
import kotlin.concurrent.thread

/**
 * 给(message: Any) -> Unit 函数类型 起别名
 */
typealias MessageCallBack = (message: Any) -> Unit

class MusicService : Service() {
    private val player by lazy { MediaPlayer() }
    private val binder by lazy { MusicPlayBinder() }
    private val allSongName = listOf("Komorebi.mp3", "岁月成碑.mp3")
    private var curIndex = 0
    private var songDuration = 0
    private var playMode: PlayMode? = null

    /**
     * 状态位，MediaPlay对象是否已经准备好了
     */
    var isPrepared = false

    /**
     * 状态位，表示当前MediaPlay对象是否可调用prepare()，
     *
     * 每调用一次prepare()就立即设为false，直到调用stop()才设为true
     */
    private var canExecFunPrepare = true

    companion object {
        lateinit var INSTANCE: MusicService
            private set
        private val callbackTasks = mutableMapOf<String, MessageCallBack>()
        private var currentCallback: MessageCallBack? = null
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> Toast.makeText(
                    this@MusicService,
                    "该歌曲文件读取失败，请更换歌曲",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        MusicService.INSTANCE = this
        // 创建通知渠道
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "YunMusicService", "音乐通知",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        // 创建通知
        val notification = NotificationCompat.Builder(this, "YunMusicService")
            .setContentTitle("通知标题")
            .setContentText("通知内容")
            .setSmallIcon(R.drawable.ic_notifications_active_24)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.oh_good))
//            .setContentIntent(intent) // 点击跳转
            .build()
        startForeground(1, notification) // 通知id为1
        player.setOnPreparedListener {
            isPrepared = true
            // 将当前已经准备好的歌曲的 总时间传递
            notifyMessageReceiver(player.duration, ServiceHelper.duration)
//            callbackTasks.remove(ServiceHelper.duration) // 一些只需要执行一次的callback会在执行后remove
        }
        player.setOnCompletionListener {
            if (!player.isLooping) {
                nextSong()
                awaitResult(5, true)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onBind(intent: Intent?): IBinder? = binder


    /**
     * 释放资源
     */
    override fun onDestroy() {
        super.onDestroy()
        player.run {
            stop()
            release()
        }
    }

    /**
     * 负责管理MusicPlay服务的binder类
     *
     * 一般情况下持有其所管理的服务的实例去操作服务
     * @constructor Create empty Music play binder
     */
    inner class MusicPlayBinder : Binder() {
        /**
         * 通过持有服务的实例的方式让其他组件可以通过binder中的service实例调用service的逻辑
         *
         * 如果这个binder类不是对应服务的内部类
         *
         * private val service: MusicService = MusicService.INSTANCE
         *
         * 如果是内部类，写法如下
         */
        val service: MusicService
            get() = this@MusicService
    }

    fun startPlay() {
        if (player.isPlaying)
            return
        player.start()
    }

    fun pausePlay() {
        if (player.isPlaying)
            player.pause()
    }

    fun nextSong() {
        if (playMode == PlayMode.PLAY_MODE_RANDOM) {
            // 待后续实现
        } else {
            var tempIndex = curIndex + 1
            if (tempIndex > allSongName.size - 1)
                tempIndex = 0
            resetPlayer()
            updateCurSong(tempIndex)
        }

    }

    fun previousSong() {
        if (playMode == PlayMode.PLAY_MODE_RANDOM) {
            // 待后续实现
        } else {
            var tempIndex = curIndex - 1
            if (tempIndex < 0)
                tempIndex = allSongName.size - 1
            resetPlayer()
            updateCurSong(tempIndex)
        }

    }

    fun updateMusicList() {

    }

    fun isPlaying() = player.isPlaying

    /**
     * 重置播放器play
     */
    private fun resetPlayer() {
        player.stop()
        player.reset()
        isPrepared = false
        canExecFunPrepare = true
    }

    /**
     * 让player进行准备
     *
     * 即将源数据载入setDataSource(),然后调用prepareAsync()或prepare()
     *
     * @param index
     */
    private fun preparePlayer(index: Int) {
        canExecFunPrepare = false
        try {
            assets.openFd(allSongName[index]).also {
                player.setDataSource(it.fileDescriptor, it.startOffset, it.length)
            }
            player.prepareAsync()
        } catch (e: IOException) {
            canExecFunPrepare = true
            e.printStackTrace()
        }
    }

    // 播放本地音乐时这个方法这样写
    /**
     * 更新当前歌曲，进行准备
     *
     * @param index
     */
    fun updateCurSong(index: Int) {
        // index有效性校验
        if (index < 0 || index >= allSongName.size) {
            Toast.makeText(this, "索引越界", Toast.LENGTH_SHORT).show()
            return
        }
        // 调用（该方法）行为有效性校验
        if (!canExecFunPrepare || isPrepared) {
            Toast.makeText(this, "目前还不能prepare()", Toast.LENGTH_SHORT).show()
            return
        }
        curIndex = index
        preparePlayer(curIndex)
    }

    /**
     * 获取音频的当前时间进度，单位毫秒
     */
    fun getCurProgress(): Int {
        return if (isPrepared)
            player.currentPosition
        else
            0
    }

//    /**
//     * 获取音频的总时间，单位毫秒
//     */
//    fun getDuration() = player.duration


    /**
     * 等待nextSong()和 previousSong()的player.prepareAsync()结果
     *
     * @param maxTime 最大等待时间 秒
     */
    fun awaitResult(maxTime: Int, state: Boolean) {
        thread {
            var times = 0
            while (times < maxTime) {
                if (isPrepared) {
                    if (state)
                        startPlay()
                    break
                } else {
                    if (canExecFunPrepare) {
                        Thread.sleep(1000)
                        preparePlayer(curIndex)
                        times++
                    }
                }
            }
            if (times >= maxTime) {
                Message().also {
                    it.what = 1
                    handler.sendMessage(it)
                }
            }
        }
    }

    /**
     * 调用此方法，通知message接收方，相当于是执行接方法写的回调
     *
     * @param message
     */
    private fun notifyMessageReceiver(message: Any, taskKey: String) {
        currentCallback = callbackTasks[taskKey] // 没有taskKey对应的call就返回null
        currentCallback?.invoke(message)
    }

    /**
     * 添加自定义的高阶函数与任务键值到回调执行任务集合里
     *
     * 作为该服务对外界的通信手段之一:   （目前获取其他信息的stringKey待定义，只有一个获取歌曲时间的duration）
     *
     * 外界只需要传入合适的stringKey，并在写lambda时直接把message强转为想从该Service里获取的数据的类型，即可获得需要的数据
     * @param taskKey
     * @param block
     */
    fun addCallbackToTasks(taskKey: String, block: MessageCallBack) {
        callbackTasks[taskKey] = block
    }

    fun seekToPosition(progress: Int) {
        player.seekTo(progress)
    }

    fun setPlayMode(mode: PlayMode) {
        playMode = mode
        player.isLooping = playMode == PlayMode.PLAY_MODE_SINGLE_CYCLE
    }
}