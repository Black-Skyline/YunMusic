package com.handsome.module.login.router

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */
interface ILoginService : IProvider {
    fun toLoginPageForResult(context: Context, onDealResult: (state: Boolean, cookies: String) -> Unit)
}