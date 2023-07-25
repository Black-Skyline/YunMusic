package com.handsome.lib.music.viewmodel

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.handsome.lib.music.utils.PlayMode
import com.handsome.lib.music.utils.PlayModeHelper
import com.handsome.lib.util.extention.toast
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
    private var timer: Timer? = null
    private var trackTaskHandler: Handler? = null
    private var trackSongProgress: Runnable? = null

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


    // 播放界面的  播放模式
    private val _playMode = MutableLiveData<PlayMode>(PlayMode.PLAY_MODE_LIST_LOOP)
    val playMode: LiveData<PlayMode>
        get() = _playMode

    // 记录进度条是否被拖拽，辅助判断
    private var isSeekbarDragging = false


    // 当前音频的图片的url列表
    private val _curAudioPicUrl = MutableLiveData<String>()
    val curAudioPicUrl: LiveData<String>
        get() = _curAudioPicUrl

    // 当前音频名称
    private val _curAudioName = MutableLiveData<String>()
    val curAudioName: LiveData<String>
        get() = _curAudioName

    // 当前创作者名称
    private val _curArtistName = MutableLiveData<String>()
    val curArtistName: LiveData<String>
        get() = _curArtistName

    // 当前音频url
    private val _curAudioUrl = MutableLiveData<String>()
    val curAudioUrl: LiveData<String>
        get() = _curAudioUrl


    /**
     * 设置音乐的播放状态，true为正播放，false为已暂停
     * @param switch
     */
    fun setPlayingState(switch: Boolean) {
        _isPlaying.value = switch
    }

    /**
     * 传入当前播放进度的获取方式
     * @param time
     * @receiver
     */
    fun setCurProgress(time: () -> Int) {
        if (timer != null)
            return
        else {
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    // 仅当 正在播放且进度条未被拖拽时
                    if (isPlaying.value!! && !isSeekbarDragging) {
                            _curProgress.postValue(time())
                    }
                }
            }, 0, 500)
        }
    }
    fun cancelTimer() {
        timer?.cancel()
        timer = null
    }

    /**
     * 传入当前播放进度的具体值
     * @param time
     * @receiver
     */
    fun setCurProgress(time: Int) {
        _curProgress.value = time
    }

    /**
     * 在服务与activity解除绑定之后移除监听任务,并释放Runnable
     */
    fun removeTrackTask() {
        trackTaskHandler?.removeCallbacks(trackSongProgress!!)
        trackSongProgress = null
        trackTaskHandler = null
    }

    /**
     * 传入歌曲总时长数据
     *
     * @param time
     */
    fun setAudioDuration(time: Int) {
        _duration.value = time
    }

    /**
     * 按一定的顺序改变播放模式
     */
    fun changePlayMode() {
        _playMode.apply {
            when (this.value!!) {
                PlayMode.PLAY_MODE_LIST_LOOP -> {
                    this.value = PlayMode.PLAY_MODE_RANDOM
                    toast(PlayModeHelper.random)
                }

                PlayMode.PLAY_MODE_RANDOM -> {
                    this.value = PlayMode.PLAY_MODE_SINGLE_CYCLE
                    toast(PlayModeHelper.single_cycle)
                }

                PlayMode.PLAY_MODE_SINGLE_CYCLE -> {
                    this.value = PlayMode.PLAY_MODE_LIST_LOOP
                    toast(PlayModeHelper.list_loop)
                }
            }
        }
    }

    /**
     * 直接改变播放模式，用于和service数据同步
     * @param mode
     */
    fun setPlayMode(mode: PlayMode) {
        _playMode.value = mode
    }

    /**
     * 传入seekbar的拖拽状态
     *
     * @param switch
     */
    fun setSeekbarDragState(switch: Boolean) {
        isSeekbarDragging = switch
    }

    fun setCurrentAudioName(audioName: String) {
        _curAudioName.value = audioName
    }

    fun setCurrentArtistName(artistName: String) {
        _curArtistName.value = artistName
    }

    fun setCurrentAudioUrl(audioUrl: String) {
        _curAudioUrl.value = audioUrl
    }

    fun setCurrentAudioPicUrl(picUrl: String) {
        _curAudioPicUrl.value = picUrl
    }
}