package com.handsome.lib.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/17
 * @Description:
 *
 */
class MusicPlayViewModel : ViewModel() {
    // 控制播放界面的 音乐的播放状态
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    // 控制播放界面的 歌词的是否展示
    private val _isShowLyric = MutableLiveData<Boolean>()
    val isShowLyric: LiveData<Boolean>
        get() = _isShowLyric

    // 当前歌曲id列表
    private val idList = mutableListOf<String>()

    // 当前歌曲id
    private val curId = MutableLiveData<String>()

    // 当前歌曲url
    private val curSongOfUrl = MutableLiveData<String>()

    fun setPlayingState(switch: Boolean){
        _isPlaying.value = switch
    }
    fun changePlayState() {
        if (_isPlaying.value!!) {
            // 暂停播放

        } else {
            // 继续播放

        }
    }

}