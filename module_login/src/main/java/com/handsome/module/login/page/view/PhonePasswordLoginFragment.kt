package com.handsome.module.login.page.view

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.util.extention.toast
import com.handsome.module.login.R
import com.handsome.module.login.databinding.FragmentPhonePasswordLoginBinding
import com.handsome.module.login.page.viewmodel.LoginViewModel
import com.handsome.module.login.router.LOGIN_PASSWORD_ENTER
import com.handsome.module.login.utils.BaseTextWatcher
import com.handsome.module.login.utils.URLUtil
import com.handsome.module.login.utils.topfuncation.CaptchaLogin
import com.handsome.module.login.utils.ValidityCheckUtil
import com.handsome.module.login.utils.topfuncation.gotoFragmentPage
import com.handsome.module.login.utils.topfuncation.setOnSingleClickListener
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
@Route(path = LOGIN_PASSWORD_ENTER)
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
                model.passwordLoginResponseFlow.collectLatest {
                    if (it != null)
                        model.dealPasswordLoginResponse(it)
                }
            }
        }
    }

    private fun initEvent() {
        binding.edtPhoneNumberToLogin.addTextChangedListener(object : BaseTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                model.isPhoneNumber2Null(text.isNullOrBlank())
                model.checkClickableLoginByPassword()
            }
        })
        binding.edtPasswordToLogin.addTextChangedListener(object : BaseTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                model.isPasswordNull(text.isNullOrBlank())
                model.checkClickableLoginByPassword()
            }
        })
    }

    private fun initObserve() {
        model.isClickableLoginByPassword.observe(viewLifecycleOwner) {
            binding.btnLoginByPassword.isEnabled = it
        }
    }

    private fun initClick() {
        binding.btnLoginByPassword.setOnSingleClickListener {
            val phoneNumber = binding.edtPhoneNumberToLogin.text?.toString()
            val password = binding.edtPasswordToLogin.text?.toString()
            if (!ValidityCheckUtil.isValidPhoneNumber(phoneNumber)) {
                toast("请按中国大陆手机号码格式正确输入手机号")
            } else if (password.isNullOrBlank()) toast("请输入密码")
            else {
                // 设定此处为成功发出登录请求的情况
                model.passwordLogin(phoneNumber!!.toLong(), URLUtil.encodeStringByUTF8(password))
            }
        }
        binding.resetPassword.setOnClickListener {

        }
    }

    private fun initView() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbarInLoginByPassword)
        }
        binding.toolbarInLoginByPassword.apply {
            // 点击返回上一个fragment
            setNavigationOnClickListener {
                gotoFragmentPage(
                    this@PhonePasswordLoginFragment,
                    CaptchaLogin, CaptchaLogin
                ) { PhoneCaptchaLoginFragment() }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("onDestroyView","Password destroy")
    }
}