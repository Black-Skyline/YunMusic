package com.handsome.lib.music.page.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.imageview.ShapeableImageView
import com.handsome.lib.music.R
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.music.sevice.MusicService
import com.handsome.lib.music.utils.MillisToTimeFormat
import com.handsome.lib.music.utils.PlayMode
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_ARTIST_NAME
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_AUDIO_DURATION
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_AUDIO_NAME
import com.handsome.lib.music.page.viewmodel.MusicPlayViewModel
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_AUDIO_CHANGE_NEXT
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_AUDIO_CHANGE_PREVIOUS
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_AUDIO_PIC_URL
import com.handsome.lib.music.utils.ServiceHelper.Companion.SENT_AUDIO_PLAY_STATE
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.MyRotationAnimate
import java.io.Serializable

class MusicPlayActivity : BaseActivity() {
    private val model by viewModels<MusicPlayViewModel>()
    lateinit var serviceOperator: MusicService

    // service是否已绑定的标志位
    private var isBinding: Boolean = false

    // 状态位，进度条是否正在被用户拖拽
    private var isSeekbarDragging = false

    // UI 元素
    private lateinit var btPlayState: ImageView
    private lateinit var tvMaxAudioTime: TextView
    private lateinit var progressBar: SeekBar
    private lateinit var tvCurrentProgress: TextView
    private lateinit var btPlayMode: ImageView
    private lateinit var btPreviousAudio: ImageView
    private lateinit var btNextAudio: ImageView
    private lateinit var btPlayList: ImageView
    private lateinit var imgAudioPicture: ShapeableImageView
    private lateinit var tvAudioName: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var btBackNavigation: ImageView

    // 动画
    private val imgRotationAnimate by lazy { MyRotationAnimate(imgAudioPicture) }
    private val imgPreviousTranslationAnimate: AnimatorSet by lazy {
        MyTranslationAnimate(
            imgAudioPicture,
            "translationX",
            1000f
        )
    }
    private val imgNextTranslationAnimate: AnimatorSet by lazy {
        MyTranslationAnimate(
            imgAudioPicture,
            "translationX",
            -1000f
        )
    }


    /**
     * 连接器Connection，负责Activity与Service的通信
     */
    private val connection = object : ServiceConnection {
        // 服务已创建完成，通信连接已建立完成，Service放入服务的具体操作者binder，然后回调该方法
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("LogicTest", "连接建立")
            isBinding = true
            serviceOperator = (service as MusicService.MusicPlayBinder).service

            val dataList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("play_list", WrapPlayInfo::class.java)
            } else {
                intent.getSerializableExtra("play_list")
            }
            val index = intent.getIntExtra("index", -1)
            if (dataList != null) serviceOperator.setPlayInfoList(
                dataList as MutableList<WrapPlayInfo>,
                index
            )
            else if (index >= 0) serviceOperator.updateCurSong(index)

            syncData2ViewModel(serviceOperator)
            overcomeUnexpected()
            Log.d("ProgressTest", "开始判断")
            if (MessageHandler != null) Log.d("LogicTest", "handle不是空的")
            if (MessageHandler == null) {
                Log.d("LogicTest", "handle是空的")
                MessageHandler = object : Handler(Looper.getMainLooper()) {
                    override fun handleMessage(msg: Message) {
                        when (msg.what) {
                            SENT_AUDIO_DURATION -> {
                                model.setAudioDuration(msg.arg1)
                            }

                            SENT_AUDIO_NAME -> {
                                model.setCurrentAudioName(msg.obj as String)
                            }

                            SENT_ARTIST_NAME -> {
                                model.setCurrentArtistName(msg.obj as String)
                            }

                            SENT_AUDIO_PIC_URL -> {
                                model.setCurrentAudioPicUrl(msg.obj as String)
                            }

                            SENT_AUDIO_CHANGE_PREVIOUS -> {
                                imgPreviousTranslationAnimate.start()
                                imgRotationAnimate.view.apply {
                                    rotation = 0f
                                    requestLayout()
                                }
                            }

                            SENT_AUDIO_CHANGE_NEXT -> {
                                imgNextTranslationAnimate.start()
                                imgRotationAnimate.view.apply {
                                    rotation = 0f
                                    requestLayout()
                                }
                            }

                            SENT_AUDIO_PLAY_STATE -> {
                                if (msg.obj as Boolean) {
                                    imgRotationAnimate.startAnimate()
                                    model.setPlayingState(true)
                                } else {
                                    imgRotationAnimate.stopAnimate()
                                    model.setPlayingState(false)
                                }
                            }
                        }
                    }
                }
            }
            /**
             * 不断的获取播放器的进度，将其同步到ViewModel中
             */
            model.setCurProgress { serviceOperator.getCurProgress() }
        }

        // 通信连接断开，回调该方法
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("LogicTest", "连接断开")
            isBinding = false
            model.cancelTimer()
            MessageHandler?.removeCallbacksAndMessages(null)
            MessageHandler = null
            model.removeTrackTask()
        }
    }

    /**
     * 如方法名，将service的数据同步到ViewModel的livedata中，以此同步此activity的UI状态
     * @param service
     */
    private fun syncData2ViewModel(service: MusicService) {
        model.setPlayMode(service.getPlayMode())
        if (service.isPrepared) model.setPlayingState(service.isPlaying())
        model.setCurrentAudioPicUrl(service.getCurAudioPicUrl())
        model.setCurProgress(service.getCurProgress())
        model.setAudioDuration(service.getCurDuration())
        model.setCurrentAudioName(service.getCurAudioName())
        model.setCurrentArtistName(service.getCurArtistName())
    }

    companion object {
        var MessageHandler: Handler? = null

        /**
         * 只携带携带播放列表启动
         * @param context    启动者的上下文
         * @param list       WrapPlayInfo对象的List，暂时用Serializable接口实现直接传递List
         * @param wantIndex  可选，第一个想要加载的音乐在list中的index
         */
        fun startWithPlayList(
            context: Context, list: MutableList<WrapPlayInfo>, wantIndex: Int? = null
        ) {
            val intent = Intent(context, MusicPlayActivity::class.java)
            intent.putExtra("play_list", list as Serializable)
            wantIndex?.let { intent.putExtra("index", wantIndex) }
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            context.startActivity(intent)
        }

        /**
         * 携带播放index启动
         * @param context     启动者的上下文
         * @param wantIndex   想要加载的音乐在list中的index
         */
        fun startWithSingleIndex(context: Context, wantIndex: Int) {
            val intent = Intent(context, MusicPlayActivity::class.java)
            intent.putExtra("index", wantIndex)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            context.startActivity(intent)
        }

        fun sentDuration(duration: Int, type: Int = SENT_AUDIO_DURATION) {
            val tempMessage = Message().also {
                it.what = type
                it.arg1 = duration
                MessageHandler?.sendMessage(it)
            }
        }

        fun sentAudioOrArtistName(name: String, type: Int) {
            val tempMessage = Message().also {
                it.what = type
                it.obj = name
                MessageHandler?.sendMessage(it)
            }
        }

        fun sentAudioOrPicUrl(url: String, type: Int) {
            val tempMessage = Message().also {
                it.what = type
                it.obj = url
                MessageHandler?.sendMessage(it)
            }
        }

        fun sentAudioNextOrPrevious(type: Int) {
            val tempMessage = Message().also {
                it.what = type
                MessageHandler?.sendMessage(it)
            }
        }

        fun sentAudioPlayState(isPlaying: Boolean) {
            val tempMessage = Message().also {
                it.what = SENT_AUDIO_PLAY_STATE
                it.obj = isPlaying
                MessageHandler?.sendMessage(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_play)

        initView()
        initClick()
        initEvent()
        initObserve()
        bindMusicPlayService()
    }

    /**
     * 排除一些因意外进入本活动的情况下的ui异常
     */
    private fun overcomeUnexpected() {
        if (serviceOperator.getPlayInfoList().isEmpty()){
            btPlayState.setImageResource(R.drawable.ic_media_click_play_60)
            imgRotationAnimate.stopAnimate()
        }
    }

    /**
     * Activity与MusicService解绑
     */
    override fun onDestroy() {
        Log.d("ProgressTest", "MusicActivity销毁了")
        super.onDestroy()
        unbindService(connection)
        model.cancelTimer()
        MessageHandler?.removeCallbacksAndMessages(null)
        MessageHandler = null
        isBinding = false
    }

    /**
     * 开启MusicPlayService
     */
    private fun bindMusicPlayService() {
        Intent(this, MusicService::class.java).also {
            bindService(it, connection, BIND_AUTO_CREATE)
        }
    }

    private fun initObserve() {
        model.isPlaying.observe(this) {
            btPlayState.apply {
                if (it) {
                    setImageResource(R.drawable.ic_media_click_pause_60)
                    imgRotationAnimate.startAnimate()
                } else {
                    setImageResource(R.drawable.ic_media_click_play_60)
                    imgRotationAnimate.stopAnimate()
                }
            }
        }
        model.duration.observe(this) {
            tvMaxAudioTime.text = MillisToTimeFormat.toMinutesAndSeconds(it)
            progressBar.max = it + 1
        }
        model.curProgress.observe(this) {
            tvCurrentProgress.text = MillisToTimeFormat.toMinutesAndSeconds(it)

            progressBar.progress = it
        }
        model.playMode.observe(this) {
            btPlayMode.apply {
                choosePlayMode(it, this)
            }
        }
        model.curAudioName.observe(this) {
            tvAudioName.text = it
        }
        model.curArtistName.observe(this) {
            tvArtistName.text = it
        }
        model.curAudioPicUrl.observe(this) {
            imgAudioPicture.setImageFromUrl(
                it,
                placeholder = R.drawable.album_picture_default,
                error = R.drawable.album_picture_default
            )
        }
    }

    /**
     * 改变播放模式时，同步改变UI
     * @param it  改变后的播放模式
     * @param ob  播放模式对应的UI image对象
     */
    private fun choosePlayMode(it: PlayMode, ob: ImageView) {
        when (it) {
            PlayMode.PLAY_MODE_SINGLE_CYCLE -> {
                ob.setImageResource(R.drawable.ic_media_playmode_single_cycle_35)
            }

            PlayMode.PLAY_MODE_LIST_LOOP -> {
                ob.setImageResource(R.drawable.ic_media_playmode_list_loop_35)
            }

            PlayMode.PLAY_MODE_RANDOM -> {
                ob.setImageResource(R.drawable.ic_media_playmode_random_35)
            }
        }
    }


    private fun initEvent() {

    }

    private fun initView() {
        btPlayState = findViewById(R.id.bt_play_select)
        tvMaxAudioTime = findViewById(R.id.tv_all_time)
        progressBar = findViewById(R.id.play_progress)
        tvCurrentProgress = findViewById(R.id.tv_cur_time)
        btPlayMode = findViewById(R.id.bt_play_mode)
        btPreviousAudio = findViewById(R.id.bt_previous_song)
        btNextAudio = findViewById(R.id.bt_next_song)
        btPlayList = findViewById(R.id.bt_playlist)
        imgAudioPicture = findViewById(R.id.iv_music_pic)
        tvAudioName = findViewById(R.id.tv_audio_name)
        tvArtistName = findViewById(R.id.tv_artist_name)
        btBackNavigation = findViewById(R.id.bt_leave_page)
        // 动画参数设置
        imgRotationAnimate.setDuration(30000)
    }

    private fun initClick() {
        /**
         * 监听底部的项
         */
        // 播放模式
        btPlayMode.setOnClickListener {
            model.changePlayMode()
            serviceOperator.setPlayMode(model.playMode.value!!)
        }
        // 上一首
        btPreviousAudio.setOnClickListener {
            serviceOperator.previousSong(model.isPlaying.value!!)
//            updateCurrentPlayInfo()
        }
        // 暂停或播放
        btPlayState.setOnClickListener {
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
                toast("音频还没准备还好，先等一下吧")
            }
        }
        // 下一首
        btNextAudio.setOnClickListener {
            serviceOperator.nextSong(model.isPlaying.value!!)
//            updateCurrentPlayInfo()
        }
        // 播放列表
        btPlayList.setOnClickListener {
            PlayListDialog(
                this,
                serviceOperator.getPlayInfoList(),
                R.style.baseBottomSheetDialog
            ).show()
        }
        // 音频图片
        imgAudioPicture

        // 监听进度条
        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(it: SeekBar?, progress: Int, b: Boolean) {
                // 用户拖动产生的进度更新，此时ViewModel里的进度数据还未更新
                progressBar.progress = progress
                tvCurrentProgress.text = MillisToTimeFormat.toMinutesAndSeconds(progress)
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

        /**
         * 监听顶部的项
         */
        // 返回
        btBackNavigation.setOnClickListener {
            finish()
        }
    }


    /**
     * 手动更新当前的播放信息
     */
    private fun updateCurrentPlayInfo() {
        model.setCurrentAudioName(serviceOperator.getCurAudioName())
        model.setCurrentArtistName(serviceOperator.getCurArtistName())
        model.setCurrentAudioPicUrl(serviceOperator.getCurAudioPicUrl())
        model.setCurProgress(serviceOperator.getCurProgress())
    }

    /**
     * 自己定义的胶片切换动画
     * @param target
     * @param name
     * @param offsetOut
     * @param duration
     * @return
     */
    private fun MyTranslationAnimate(
        target: View,
        name: String,
        offsetOut: Float,
        duration: Long = 200
    ): AnimatorSet {
        val animator1 = ObjectAnimator.ofFloat(target, name, target.translationX, offsetOut)
        val animator2 = ObjectAnimator.ofFloat(
            target,
            name,
            target.translationX - offsetOut,
            target.translationX
        )
        val set = AnimatorSet()
        set.playSequentially(animator1, animator2)
        set.duration = duration
        return set
    }

    override fun onStop() {
        super.onStop()
        imgRotationAnimate.stopAnimate()
    }
}



