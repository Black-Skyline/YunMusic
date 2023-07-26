package com.handsome.module.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout,MineFragment())
            .commit()
    }
}