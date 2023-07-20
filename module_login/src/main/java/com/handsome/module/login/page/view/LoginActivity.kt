package com.handsome.module.login.page.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.handsome.lib.util.BaseApp
import com.handsome.module.login.R
import com.handsome.module.login.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.entrance_fragment_place,
                PhoneCaptchaLoginFragment(),
                "PhoneCaptchaLoginFragment_INSTATE"
            )
            .commit()
    }
}