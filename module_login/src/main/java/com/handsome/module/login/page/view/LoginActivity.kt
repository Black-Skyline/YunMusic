package com.handsome.module.login.page.view


import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.login.R
import com.handsome.module.login.databinding.ActivityLoginBinding
import com.handsome.module.login.router.ILoginService
import com.handsome.module.login.router.LOGIN_GATE

@Route(path = LOGIN_GATE)
class LoginActivity : BaseActivity() {
//    var callback: (() -> Unit)? = null

    private
    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().apply {
                add(
                    R.id.entrance_fragment_container,
                    PhoneCaptchaLoginFragment(),
                    "PhoneCaptchaLogin_INSTANCE"
                )
                commit()
            }
//        ARouter.getInstance().navigation(ILoginService::class.java)
//            .toLoginPageForResult(this) { b: Boolean, s: String ->
//                // 这个b就是我传给你的状态位，s就是我给你的cookies
//            }
    }

    fun finishLogin() {
        finish()
    }

    companion object {
        var resultCallback: ((state: Boolean, cookies: String) -> Unit)? = null
        fun startActivity(context: Context, callback: (state: Boolean, cookies: String) -> Unit) {
            context.startActivity(Intent(context, LoginActivity::class.java))
            resultCallback = callback
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (LoginActivity.resultCallback != null)
            resultCallback = null
    }
}