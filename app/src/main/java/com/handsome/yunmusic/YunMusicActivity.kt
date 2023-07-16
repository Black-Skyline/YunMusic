package com.handsome.yunmusic

import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity

open class YunMusicActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler())
    }
}