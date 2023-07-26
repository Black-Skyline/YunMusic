package com.handsome.module.login.page.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.login.R
import com.handsome.module.login.databinding.ActivityLoginBinding

@Route(path = "/login/gate")
class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
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
    }
}