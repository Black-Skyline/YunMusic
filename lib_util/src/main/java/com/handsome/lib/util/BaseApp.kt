package com.handsome.lib.util

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2023649401@qq.com
 * @date 2023/7/16
 * @Description:
 */
open class BaseApp : Application() {
    companion object {
        lateinit var mContext: BaseApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        if (true) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }
}
