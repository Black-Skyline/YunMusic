package com.handsome.lib.util.util

import android.content.Context
import android.content.SharedPreferences
import com.handsome.lib.util.BaseApp

fun getSharePreference(name: String): SharedPreferences {
    return BaseApp.mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
}