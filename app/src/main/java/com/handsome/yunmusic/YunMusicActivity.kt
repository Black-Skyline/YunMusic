package com.handsome.yunmusic

import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.util.getSharePreference

open class YunMusicActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        //只进行一次异常捕获
//        if (intent.getBooleanExtra("restart",true)) {
//            Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler())
//        }
    }
}