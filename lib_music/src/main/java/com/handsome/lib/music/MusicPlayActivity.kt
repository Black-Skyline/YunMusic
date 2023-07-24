package com.handsome.lib.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.music.sevice.MusicService
import com.handsome.lib.music.utils.MillisToTimeFormat
import com.handsome.lib.music.utils.PlayMode
import com.handsome.lib.music.utils.ServiceHelper
import com.handsome.lib.music.viewmodel.MusicPlayViewModel
import java.io.Serializable

class MusicPlayActivity : AppCompatActivity() {
    private val model by viewModels<MusicPlayViewModel>()
    private lateinit var serviceOperator: MusicService

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

    /**
     * 连接器Connection，负责Activity与Service的通信
     */
    private val connection = object : ServiceConnection {
        // 服务已创建完成，通信连接已建立完成，Service放入服务的具体操作者binder，然后回调该方法
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isBinding = true
            serviceOperator = (service as MusicService.MusicPlayBinder).service


            val dataList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("play_list", WrapPlayInfo::class.java)
            } else {
                intent.getSerializableExtra("play_list")
            }
            val index = intent.getIntExtra("index", -1)
            if (dataList != null)
                serviceOperator.setPlayInfoList(dataList as MutableList<WrapPlayInfo>, index)
            else
                serviceOperator.updateCurSong(index)
            /*  测试用，未加入picUrl
            val tempData = mutableListOf<WrapPlayInfo>()

            tempData.add(WrapPlayInfo("나 어떡해 ('77 제1회 MBC대학가요제 대상')", "V.A.", 2064562707))
            tempData.add(WrapPlayInfo("画", "赵雷", 202369))
            tempData.add(WrapPlayInfo("独一无二的美丽", "李萌", 2065902610))
            tempData.add(WrapPlayInfo("逝年", "夏小虎", 32957955))

            serviceOperator.setPlayInfoList(tempData, 0)
            */
            syncData2ViewModel(serviceOperator)
//            serviceOperator.updateCurSong(0)
//            serviceOperator.setPlayMode(model.playMode.value!!)
            /**
             * 不断的获取播放器的进度，将其同步到ViewModel中
             */
            model.setCurProgress { serviceOperator.getCurProgress() }
            /**
             * 添加回调任务：播放器准备完成后传递duration、audioName、artistName值给model
             */
            serviceOperator.addCallbackToTasks(ServiceHelper.duration) {
                model.setSongDuration(it as Int)
            }
            serviceOperator.addCallbackToTasks(ServiceHelper.audioName) {
                model.setCurrentAudioName(it as String)
            }
            serviceOperator.addCallbackToTasks(ServiceHelper.artistName) {
                model.setCurrentArtistName(it as String)
            }
        }

        // 通信连接断开，回调该方法
        override fun onServiceDisconnected(name: ComponentName?) {
            isBinding = false
            model.removeTrackTask()
        }
    }

    /**
     * 如方法名，将service的数据同步到ViewModel的livedata中，以此同步此activity的UI状态
     * @param service
     */
    private fun syncData2ViewModel(service: MusicService) {
        model.setPlayMode(service.getPlayMode())
        model.setPlayingState(service.isPrepared && service.isPlaying())
        model.setCurrentAudioName(service.getCurSongName())
        model.setCurrentArtistName(service.getCurArtistName())
//        model.setCurrentAudioUrl()
    }

    companion object {
        /**
         * 携带播放列表启动
         * @param context    启动者的上下文
         * @param list       WrapPlayInfo对象的List，暂时用Serializable接口实现直接传递List
         * @param wantIndex  可选，第一个想要加载的音乐在list中的index
         */
        fun startWithPlayList(
            context: Context,
            list: MutableList<WrapPlayInfo>,
            wantIndex: Int? = null
        ) {
            val intent = Intent(context, MusicPlayActivity::class.java)
            intent.putExtra("play_list", list as Serializable)
            wantIndex?.let { intent.putExtra("index", wantIndex) }
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
            context.startActivity(intent)
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
    private fun bindMusicPlayService() {
        Intent(this, MusicService::class.java).also {
            bindService(it, connection, BIND_AUTO_CREATE)
        }

    }

    private fun initObserve() {
        model.isPlaying.observe(this) {
            btPlayState.apply {
                if (it)
                    setImageResource(R.drawable.ic_media_click_pause_60)
                else
                    setImageResource(R.drawable.ic_media_click_play_60)
            }
        }
        model.duration.observe(this) {
            tvMaxAudioTime.text =
                MillisToTimeFormat.toMinutesAndSeconds(it)
            progressBar.max = it
        }
        model.curProgress.observe(this) {
            tvCurrentProgress.text =
                MillisToTimeFormat.toMinutesAndSeconds(it)

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
//                Toast.makeText(
//                    this@MusicPlayActivity,
//                    PlayModeHelper.single_cycle,
//                    Toast.LENGTH_LONG
//                ).show()
            }

            PlayMode.PLAY_MODE_LIST_LOOP -> {
                ob.setImageResource(R.drawable.ic_media_playmode_list_loop_35)

            }

            PlayMode.PLAY_MODE_RANDOM -> {
                ob.setImageResource(R.drawable.ic_media_playmode_single_cycle_35)

            }
        }
    }


    private fun initEvent() {

    }

    private fun initView() {
        btPlayState = findViewById<ImageView>(R.id.bt_play_select)
        tvMaxAudioTime = findViewById<TextView>(R.id.tv_all_time)
        progressBar = findViewById<SeekBar>(R.id.play_progress)
        tvCurrentProgress = findViewById<TextView>(R.id.tv_cur_time)
        btPlayMode = findViewById<ImageView>(R.id.bt_play_mode)
        btPreviousAudio = findViewById<ImageView>(R.id.bt_previous_song)
        btNextAudio = findViewById<ImageView>(R.id.bt_next_song)
        btPlayList = findViewById<ImageView>(R.id.bt_playlist)
        imgAudioPicture = findViewById<ShapeableImageView>(R.id.iv_music_pic)
        tvAudioName = findViewById<TextView>(R.id.tv_audio_name)
        tvArtistName = findViewById<TextView>(R.id.tv_artist_name)
        btBackNavigation = findViewById<ImageView>(R.id.bt_leave_page)

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
            serviceOperator.previousSong()
            serviceOperator.awaitResult(5, model.isPlaying.value!!)
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
                serviceOperator.updateCurSong(0)
            }
        }
        // 下一首
        btNextAudio.setOnClickListener {
            serviceOperator.nextSong()
            serviceOperator.awaitResult(5, model.isPlaying.value!!)
        }
        // 播放列表
        btPlayList
        // 音频图片
        imgAudioPicture

        // 监听进度条
        progressBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
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
}



