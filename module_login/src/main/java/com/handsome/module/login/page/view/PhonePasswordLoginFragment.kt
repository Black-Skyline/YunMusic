package com.handsome.module.login.page.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.handsome.module.login.databinding.FragmentPhonePasswordLoginBinding
import com.handsome.module.login.page.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/19
 * @Description:
 *
 */
class PhonePasswordLoginFragment : Fragment() {
    private var _binding: FragmentPhonePasswordLoginBinding? = null
    private val binding: FragmentPhonePasswordLoginBinding
        get() = _binding!!
    private val model by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhonePasswordLoginBinding.inflate(inflater, container, false)
        initView()
        initClick()
        initObserve()
        initEvent()
        initSubscribe()
        return binding.root
    }

    private fun initSubscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }

    }

    private fun initEvent() {

    }

    private fun initObserve() {


    }

    private fun initClick() {


    }

    private fun initView() {

    }
}