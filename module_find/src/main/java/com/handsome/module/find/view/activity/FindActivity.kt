package com.handsome.module.find.view.activity

import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.find.R
import com.handsome.module.find.databinding.ActivityFindBinding
import com.handsome.module.find.view.fragment.FindFragment

class FindActivity : BaseActivity() {
    private val mBinding by lazy { ActivityFindBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        val test = FindFragment()
        supportFragmentManager.beginTransaction().replace(R.id.abc, test).commit()
    }
}