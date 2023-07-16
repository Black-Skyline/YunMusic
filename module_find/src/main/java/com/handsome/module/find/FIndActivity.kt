package com.handsome.module.find

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FIndActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        val test = BlankFragment()
        supportFragmentManager.beginTransaction().replace(R.id.abc, test).commit()
    }
}