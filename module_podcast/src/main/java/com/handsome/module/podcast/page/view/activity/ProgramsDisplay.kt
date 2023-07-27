package com.handsome.module.podcast.page.view.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.handsome.lib.music.sevice.MusicService
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.podcast.page.viewmodel.ProgramsListDetailViewModel

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */
class ProgramsDisplay : BaseActivity(){
    private val model by lazy { ViewModelProvider(this)[ProgramsListDetailViewModel::class.java] }

    // Service实例
    private lateinit var mMusicService: MusicService  // 可播放具体节目

    companion object {
        fun startAction(context: Activity, id: Long) {
            val intent = Intent(context, ProgramsDisplay::class.java)
            intent.putExtra("rid", id)
            context.startActivity(intent)
        }
    }
}