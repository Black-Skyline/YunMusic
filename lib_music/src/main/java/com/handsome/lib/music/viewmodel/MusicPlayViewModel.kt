package com.handsome.lib.music.viewmodel

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.handsome.lib.music.utils.PlayMode
import java.util.Timer
import java.util.TimerTask

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/17
 * @Description:
 *
 */
class MusicPlayViewModel : ViewModel() {

    // 帮助后台线程获取数据后，完成setValue
    val handler = android.os.Handler(Looper.getMainLooper())

    // 控制播放界面的 音乐的播放状态
    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    // 控制播放界面的 歌词的是否展示
    private val _isShowLyric = MutableLiveData<Boolean>(false)
    val isShowLyric: LiveData<Boolean>
        get() = _isShowLyric

    // 控制播放界面的  进度条进度
    private val _curProgress = MutableLiveData<Int>(0)
    val curProgress: LiveData<Int>
        get() = _curProgress

    // 控制播放界面的  总时间
    private val _duration = MutableLiveData<Int>(0)
    val duration: LiveData<Int>
        get() = _duration

    // 播放界面的歌曲名称
    private val _songName = MutableLiveData<String>("")
    val songName: LiveData<String>
        get() = _songName

    // 播放界面的播放模式
    private val _playMode = MutableLiveData<PlayMode>(PlayMode.PLAY_MODE_LIST_LOOP)
    val playMode: LiveData<PlayMode>
        get() = _playMode

    // 记录进度条是否被拖拽，辅助判断
    private var isSeekbarDragging = false

    // 当前歌曲id列表
    private val idList = mutableListOf<String>()

    // 当前歌曲id
    private val curId = MutableLiveData<String>()

    // 当前歌曲url
    private val curSongOfUrl = MutableLiveData<String>()

    /**
     * 设置音乐的播放状态，true为正播放，false为已暂停
     * @param switch
     */
    fun setPlayingState(switch: Boolean) {
        _isPlaying.value = switch
    }

    /**
     * 传入当前播放进度数据
     *
     * @param time
     * @receiver
     */
    fun setCurProgress(time: () -> Int) {
        val trackSongProgress = object : TimerTask() {
            override fun run() {
                // 仅当 正在播放且进度条未被拖拽时
                if (isPlaying.value!! && !isSeekbarDragging) {
                    handler.post {
                        _curProgress.value = time()
                    }
                }
            }
        }
        Timer().schedule(trackSongProgress, 0, 200)
    }

    /**
     * 传入歌曲总时长数据
     *
     * @param time
     */
    fun setSongDuration(time: Int) {
        _duration.value = time
    }

    /**
     * 改变播放模式
     *
     */
    fun changePlayMode() {
        _playMode.apply {
            when (this.value!!) {
                PlayMode.PLAY_MODE_LIST_LOOP -> this.value = PlayMode.PLAY_MODE_RANDOM
                PlayMode.PLAY_MODE_RANDOM -> this.value = PlayMode.PLAY_MODE_SINGLE_CYCLE
                PlayMode.PLAY_MODE_SINGLE_CYCLE -> this.value = PlayMode.PLAY_MODE_LIST_LOOP
            }
        }
    }

    /**
     * 传入seekbar的拖拽状态
     *
     * @param switch
     */
    fun setSeekbarDragState(switch: Boolean) {
        isSeekbarDragging = switch
    }
}