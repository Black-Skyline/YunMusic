package com.handsome.module.find

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.handsome.lib.util.base.BaseFragment
import com.handsome.module.find.databinding.FragmentBlankBinding


class BlankFragment : BaseFragment() {
    private val mBinding by lazy { FragmentBlankBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = BlankFragment()

    }
}