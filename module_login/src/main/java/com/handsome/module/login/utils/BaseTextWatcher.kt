package com.handsome.module.login.utils

import android.text.Editable
import android.text.TextWatcher

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/20
 * @Description:
 *
 */
open class BaseTextWatcher(code: (() -> Unit)? = null) : TextWatcher {
    private val theCode = code
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(text: Editable?) {
        theCode?.invoke()
    }
}