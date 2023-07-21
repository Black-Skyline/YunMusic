package com.handsome.lib.search.network

import com.handsome.lib.util.extention.toast
import kotlinx.coroutines.CoroutineExceptionHandler

val myCoroutineExceptionHandler = CoroutineExceptionHandler{ _,e ->
    e.printStackTrace()
    e.toString().toast()
}