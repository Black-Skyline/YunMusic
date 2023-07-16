package com.handsome.module.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.handsome.lib.util.BaseApp

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<View>(R.id.textView).setOnClickListener {
            Toast.makeText(BaseApp.mContext, "可以调用",Toast.LENGTH_LONG).show()
        }
    }
}