package com.handsome.lib.music.sevice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.handsome.lib.music.R
import java.io.IOException

class MusicService : Service() {
    private val player by lazy { MediaPlayer() }
    private val binder by lazy { MusicPlayBinder() }
    private val allSongName = listOf("Komorebi.mp3", "岁月成碑.mp3")
    private var curIndex = 0

    // 状态位，MediaPlay对象已调用prepare设为true
    var isPrepare = false

    // 状态位，表示当前MediaPlay对象是否可调用prepare()，调用一次prepare()就设为false，直到调用了stop()设为true
    var canExecFunPrepare = true

    companion object {
        lateinit var INSTANCE: MusicService
            private set
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
            isPrepare = true
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
        isPrepare = false
        canExecFunPrepare = true
        var tempIndex = curIndex + 1
        if (tempIndex > allSongName.size - 1)
            tempIndex = 0
        updateCurSong(tempIndex)
    }

    fun previousSong() {
        isPrepare = false
        canExecFunPrepare = true
        var tempIndex = curIndex - 1
        if (tempIndex < 0)
            tempIndex = allSongName.size - 1
        updateCurSong(tempIndex)
    }

    fun updateMusicList() {

    }

    fun isPlaying() = player.isPlaying

    // 播放本地音乐时这个方法这样写
    fun updateCurSong(index: Int) {
        if (index < 0 || index >= allSongName.size) {
            Toast.makeText(this, "索引越界", Toast.LENGTH_SHORT).show()
            return
        }
        if (!canExecFunPrepare || isPrepare) {
            Toast.makeText(this, "目前还不能prepare()", Toast.LENGTH_SHORT).show()
            return
        }

        curIndex = index
        try {
            isPrepare = false
            assets.openFd(allSongName[index]).also {
                player.setDataSource(it.fileDescriptor, it.startOffset, it.length)
            }
            if (canExecFunPrepare) {
                player.prepareAsync()
                canExecFunPrepare = false
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}