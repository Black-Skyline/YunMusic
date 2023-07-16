package com.handsome.module.find

import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.find.databinding.ActivityFindBinding

class FindActivity : BaseActivity() {
    private val mBinding by lazy { ActivityFindBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        val test = BlankFragment()
        supportFragmentManager.beginTransaction().replace(R.id.abc, test).commit()
    }
}