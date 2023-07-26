package com.handsome.lib.music.sevice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.handsome.lib.music.page.view.MusicPlayActivity
import com.handsome.lib.music.R
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.music.network.api.AudioUrlApiService
import com.handsome.lib.music.room.AppDatabase
import com.handsome.lib.music.room.LatestMusicDao
import com.handsome.lib.music.utils.PlayMode
import com.handsome.lib.music.utils.RandomNumber
import com.handsome.lib.music.utils.ServiceHelper.Companion.CLOSE
import com.handsome.lib.music.utils.ServiceHelper.Companion.NEXT
import com.handsome.lib.music.utils.ServiceHelper.Companion.PLAY
import com.handsome.lib.music.utils.ServiceHelper.Companion.PREV
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_ARTIST_NAME
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_AUDIO_CHANGE_NEXT
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_AUDIO_CHANGE_PREVIOUS
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_AUDIO_NAME
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_AUDIO_PIC_URL
import com.handsome.lib.music.utils.ServiceHelper.Companion.channelID
import com.handsome.lib.util.extention.toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException


class MusicService : Service() {
    private val player by lazy { MediaPlayer() }
    private val binder by lazy { MusicPlayBinder() }

    private lateinit var recorder: LatestMusicDao

    //    private val allSongName = listOf("Komorebi.mp3", "岁月成碑.mp3")
    private var playInfoList = mutableListOf<WrapPlayInfo>()
    private var curPlayInfo: WrapPlayInfo? = null
    private var curIndex = 0
    private var curAudioUrlList = mutableListOf<String>()

    private var playMode: PlayMode = PlayMode.PLAY_MODE_LIST_LOOP

    /**
     * 关于通知栏的几个变量
     */
    private var notificationView: RemoteViews? = null
    private var notificationActionReceiver: NotificationActionReceiver? = null
    private var manager: NotificationManager? = null
    private var notification: Notification? = null

    /**
     * 状态位，MediaPlay对象是否已经准备好了
     */
    var isPrepared = false

    /**
     * 状态位，音频的url是否准备好了
     */
    var isUrlPrepared = false

    /**
     * 状态位，表示当前MediaPlay对象是否可调用prepare()，
     *
     * 每调用一次prepare()就立即设为false，直到调用stop()才设为true
     */
    private var canExecFunPrepare = true

    /**
     * 状态位，是否自动播放
     */
    private var isAutoPlay = true

    /**
     * 状态位，当前服务是否为前台服务
     */
    private var isForeground = false

    companion object {
        lateinit var INSTANCE: MusicService
            private set
    }

    private var networkHandler: Handler? = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> { // 加载网络图片到通知栏
                    notificationView?.setImageViewBitmap(R.id.iv_album_cover, msg.obj as Bitmap)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        recorder = AppDatabase.getDataBase().wrapPlayInfoDao()
        initNotification()

        player.setOnPreparedListener {
            isPrepared = true
            MusicPlayActivity.sentDuration(getCurDuration())

            if (isAutoPlay) startPlay()
            Log.d("ProgressTest", "OnPrepared回调了")
        }

        player.setOnCompletionListener {
            if (!player.isLooping) {
                nextSong()
                Log.d("ProgressTest", "OnCompletion回调了")
            }
        }
    }


    /**
     * 初始化通知所必须的一些东西
     */
    private fun initNotification() {
        initNotificationUI()
        // 创建通知渠道
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelID, "音乐通知", NotificationManager.IMPORTANCE_LOW)
            channel.apply {
                enableVibration(false)
                setSound(null, null)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                manager!!.createNotificationChannel(this)
            }

        }
        // 构建一个目标为MusicPlayActivity的延迟Intent
        val targetIntent = Intent(
            this, MusicPlayActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        val pendingToActivity =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.getActivity(
                this,
                0,
                targetIntent,
                FLAG_IMMUTABLE
            )
            else PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 创建通知
        notification = NotificationCompat.Builder(this, channelID).setContentTitle("通知标题")
            .setSound(null)                        // 通知不发出响声
            .setSmallIcon(R.drawable.ic_notifications_active_24)
            .setCustomBigContentView(notificationView).setAutoCancel(false)
//                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.oh_good))
            .setContentIntent(pendingToActivity)   // 点击跳转
            .build()
        notificationView?.setOnClickPendingIntent(R.id.notification_layout, pendingToActivity)
        registerNotificationReceiver()
    }

    private fun showNotification() {
        if (isPlaying()) {
            notificationView?.setImageViewResource(
                R.id.notification_btn_play, R.drawable.notification_icon_click_pause
            )
        } else {
            notificationView?.setImageViewResource(
                R.id.notification_btn_play, R.drawable.notification_icon_click_play
            )
        }
        //加载封面专辑
//        Glide.with(this).asBitmap().load(curPlayInfo!!.picUrl).apply(RequestOptions())
//            .into(object : SimpleTarget<Bitmap>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    resource.apply {
//                        networkHandler?.sendMessage(Message().also {
//                            it.what= 1
//                            it.obj = this
//                        })
//                    }
//                }
//            })
//        notificationView.setImageViewBitmap(R.id.iv_album_cover, curPlayInfo!!.picUrl)
        //歌曲名
        notificationView?.setTextViewText(R.id.notification_tv_audio_name, curPlayInfo!!.audioName)
        //歌手名
        notificationView?.setTextViewText(
            R.id.notification_tv_artist_name, curPlayInfo!!.artistName
        )
    }


    /**
     * 注册通知栏行为的广播接收器
     */
    private fun registerNotificationReceiver() {
        notificationActionReceiver = NotificationActionReceiver()
        IntentFilter().apply {
            addAction(PLAY)
            addAction(PREV)
            addAction(NEXT)
            addAction(CLOSE)
            registerReceiver(notificationActionReceiver, this)
        }
    }

    private fun initNotificationUI() {
        notificationView = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            RemoteViews(this.packageName, R.layout.view_notification_layout, FLAG_IMMUTABLE)
        } else RemoteViews(this.packageName, R.layout.view_notification_layout)
        // 通知栏位置的上一首功能
        val prevPendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.getBroadcast(
                this,
                0,
                Intent(PREV),
                FLAG_IMMUTABLE
            )
            else PendingIntent.getBroadcast(
                this,
                0,
                Intent(PREV),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        notificationView!!.setOnClickPendingIntent(
            R.id.notification_btn_previous, prevPendingIntent
        )

        // 通知栏位置的播放、暂停功能
        val playPendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.getBroadcast(
                this,
                0,
                Intent(PLAY),
                FLAG_IMMUTABLE
            )
            else PendingIntent.getBroadcast(
                this,
                0,
                Intent(PLAY),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        notificationView!!.setOnClickPendingIntent(R.id.notification_btn_play, playPendingIntent)

        // 通知栏位置的下一首功能
        val nextPendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.getBroadcast(
                this,
                0,
                Intent(NEXT),
                FLAG_IMMUTABLE
            )
            else PendingIntent.getBroadcast(
                this,
                0,
                Intent(NEXT),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        notificationView!!.setOnClickPendingIntent(R.id.notification_btn_next, nextPendingIntent)

        // 通知栏位置的“关闭”功能
        val closePendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.getBroadcast(
                this,
                0,
                Intent(CLOSE),
                FLAG_IMMUTABLE
            )
            else PendingIntent.getBroadcast(
                this,
                0,
                Intent(CLOSE),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        notificationView!!.setOnClickPendingIntent(R.id.btn_notification_close, closePendingIntent)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onBind(intent: Intent?): IBinder = binder


    /**
     * 释放资源
     */
    override fun onDestroy() {
        super.onDestroy()
        player.run {
            stop()
            release()
        }
        networkHandler = null
        if (notificationActionReceiver != null) {
            // 解除动态注册的广播接收器
            unregisterReceiver(notificationActionReceiver)
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

    inner class NotificationActionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.action?.let { notificationActionCommand(it) }
        }
    }

    /**
     * 对通知发出的不同行为做出对应处理
     * @param action
     */
    private fun notificationActionCommand(action: String) {
        when (action) {
            PLAY -> {
                if (player.isPlaying) pausePlay()
                else startPlay()
            }

            PREV -> {
                previousSong()
            }

            NEXT -> {
                nextSong()
            }

            CLOSE -> {
                // 关闭通知，转入后台
                stopForeground(true)
                isForeground = false
            }
        }
    }


    /**
     * 开启音乐播放需要调用的方法
     */
    fun startPlay() {
        if (player.isPlaying) return
        MusicPlayActivity.sentAudioPlayState(true)
        if (isForeground) showNotification()
        else {
            startForeground(1, notification)
            showNotification()
        }
        player.start()
        curPlayInfo?.let { addPlaybackHistory(it) }
    }

    fun pausePlay() {
        if (player.isPlaying) {
            player.pause()
            MusicPlayActivity.sentAudioPlayState(false)
        }
        showNotification()
    }

    fun nextSong(auto: Boolean = true) {
        if (!isUrlPrepared) {  // 异常情况（比如没网、数据加载失败）处理
            toast("还没加载好，等下再试吧")
            return
        }

        var tempIndex: Int
        if (playMode == PlayMode.PLAY_MODE_RANDOM) {
            // 待后续实现
            tempIndex = RandomNumber.getNumberExcludeAppoint(playInfoList.size, curIndex)
        } else {
            tempIndex = curIndex + 1
            if (tempIndex > playInfoList.size - 1) tempIndex = 0
        }
        isAutoPlay = auto
        MusicPlayActivity.sentAudioNextOrPrevious(SENT_AUDIO_CHANGE_NEXT)
        updateCurSong(tempIndex)
    }

    fun previousSong(auto: Boolean = true) {  // 异常情况（比如没网、数据加载失败）处理
        if (!isUrlPrepared) {
            toast("还没加载好，等下再试吧")
            return
        }

        var tempIndex: Int
        if (playMode == PlayMode.PLAY_MODE_RANDOM) {
            // 待后续实现
            tempIndex = RandomNumber.getNumberExcludeAppoint(playInfoList.size, curIndex)
        } else {
            tempIndex = curIndex - 1
            if (tempIndex < 0) tempIndex = playInfoList.size - 1
        }
        isAutoPlay = auto
        MusicPlayActivity.sentAudioNextOrPrevious(SENT_AUDIO_CHANGE_PREVIOUS)
        updateCurSong(tempIndex)
    }

    fun updateMusicList() {

    }

    /**
     * 获取播放器的播放状态
     */
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
            player.setDataSource(playInfoList[index].audioUrl)
            player.prepareAsync()
        } catch (e: IOException) {
            canExecFunPrepare = true
            Log.d("LogicTest", "异常的url${playInfoList[index].audioUrl}")
            toast("音频准备异常")
            e.printStackTrace()
        }
    }

    private fun setCurrentPlayInfo(info: WrapPlayInfo) {
        curPlayInfo = info
    }


    /**
     * 更新当前歌曲，对其进行准备，index为其在列表中的位置
     *
     * @param index 当前想播放的音频在播放列表里index（位置）
     */
    fun updateCurSong(index: Int) {
        resetPlayer()
        // url数据准备完成校验
        if (!isUrlPrepared) return
        // index有效性校验
        if (index < 0 || index >= playInfoList.size) {
            Toast.makeText(this, "索引越界", Toast.LENGTH_SHORT).show()
            return
        }
        // 调用（该方法）行为有效性校验
        if (!canExecFunPrepare || isPrepared) {
            Log.d("LogicTest", "isPrepared ${isPrepared}, canExecFunPrepare ${canExecFunPrepare}")
            Toast.makeText(this, "目前还不能prepare()", Toast.LENGTH_SHORT).show()
            return
        }
        curIndex = index
        setCurrentPlayInfo(playInfoList[index])
        showNotification()
        MusicPlayActivity.sentAudioOrArtistName(getCurAudioName(), SENT_AUDIO_NAME)
        MusicPlayActivity.sentAudioOrArtistName(getCurArtistName(), SENT_ARTIST_NAME)
        MusicPlayActivity.sentAudioOrPicUrl(getCurAudioPicUrl(), SENT_AUDIO_PIC_URL)
        preparePlayer(curIndex)
    }


    fun seekToPosition(progress: Int) {
        player.seekTo(progress)
    }

    /**
     * 加入播放记录
     * @param record
     */
    private fun addPlaybackHistory(record: WrapPlayInfo) {
        recorder.insertMusic(record).subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { Log.d("LogicTest", "播放记录写入成功") }
            .subscribe(object : SingleObserver<Long> {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    Log.d("LogicTest", "加入播放历史失败，错误为${e.message}")
                    toast("加入播放历史失败")
                }

                override fun onSuccess(t: Long) {
                    Log.d("LogicTest", "加入播放历史成功，id为$t")
                }
            })
    }

    /**
     * 设置播放模式
     * @param mode
     */
    fun setPlayMode(mode: PlayMode) {
        playMode = mode
        player.isLooping = playMode == PlayMode.PLAY_MODE_SINGLE_CYCLE
    }

    /**
     * 获取服务当前的playMode
     */
    fun getPlayMode() = playMode

    /**
     * 传入要设置给播放器和播放界面的信息
     * @param list       必选，歌单的基本播放信息
     * @param wantIndex  可选，需要时再用
     */
    fun setPlayInfoList(list: MutableList<WrapPlayInfo>, wantIndex: Int = 0) {
        if (list.isEmpty()) {
            toast("数据设置失败，空列表数据")
            return
        }

        playInfoList = list
        isUrlPrepared = false  // 传入了新的歌单，url需要重新解析
        val idStr = StringBuilder()
        for (item in playInfoList) {
            idStr.append("${item.audioId},")
        }
        val result = idStr.toString().dropLast(1)
        val getUrlTask =
            AudioUrlApiService.INSTANCE.getMultipleUrlResponse(result).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    curAudioUrlList.removeAll(curAudioUrlList)
                    for (i in it.data) {
                        for (item in playInfoList) {
                            if (item.audioId == i.id) {
                                item.audioUrl = i.url
                                curAudioUrlList.add(i.url)
                            }
                        }
                    }
                    isUrlPrepared = !playInfoList[wantIndex].audioUrl.isNullOrBlank()
                    resetPlayer()
                    if (isUrlPrepared && canExecFunPrepare) {
                        updateCurSong(wantIndex)
                    }
                }, {
                    // 处理异常情况下的订阅事件
                    toast("url请求出了问题")
                })
    }

    /**
     * 根据传入的index找到当前播放信息列表中对应的歌曲名
     *
     * @param index
     * @return AudioName
     */
    fun getAudioNameByIndex(index: Int): String {
        return if (playInfoList.isNotEmpty()) {
            playInfoList[index].audioName
        } else "网络歌手"
    }

    /**
     * 向外暴露数据的get方法
     */
    fun getCurAudioName() = if (curPlayInfo == null) "不知名专辑" else curPlayInfo!!.audioName

    fun getCurArtistName() = if (curPlayInfo == null) "网络歌手" else curPlayInfo!!.artistName

    fun getCurAudioId() = if (curPlayInfo == null) 0 else curPlayInfo!!.audioId

    fun getCurAudioUrl() = if (curPlayInfo == null) "找不到" else curPlayInfo!!.audioUrl

    fun getCurAudioPicUrl() = if (curPlayInfo == null) "找不到" else curPlayInfo!!.picUrl

    /**
     * 获取当前音频的当前时间进度，单位毫秒
     */
    fun getCurProgress(): Int {
        return if (isPrepared) player.currentPosition
        else 0
    }

    /**
     * 获取当前音频的总时长，单位毫秒
     */
    fun getCurDuration(): Int {
        return if (isPrepared) player.duration
        else 0
    }

    /**
     * 获取当前音频的播放信息
     */
    fun getCurPlayInfo() = curPlayInfo

    /**
     * 获取当前的音频播放列表
     */
    fun getPlayInfoList() = playInfoList
}