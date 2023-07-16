package com.handsome.module.find

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.handsome.module.find.databinding.FragmentFindBinding


class FindFragment : Fragment() {
   private val mBinding by lazy { FragmentFindBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FindFragment()
    }
}