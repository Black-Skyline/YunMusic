package com.handsome.lib.util.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() , View.OnClickListener{
    override fun onClick(v: View?){}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.view?.isClickable = false
        Log.d("lx", "onDestroy:${javaClass.name} fragment已经摧毁")
    }
}