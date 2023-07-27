package com.handsome.module.login.router

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.module.login.page.view.LoginActivity

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */
@Route(path = LOGIN_SERVICE)
class LoginServiceImpl : ILoginService {
    override fun toLoginPageForResult(
        context: Context,
        onDealResult: (state: Boolean, cookies: String) -> Unit
    ) {
        LoginActivity.startActivity(context, onDealResult)
    }


    override fun init(context: Context?) {

    }

}