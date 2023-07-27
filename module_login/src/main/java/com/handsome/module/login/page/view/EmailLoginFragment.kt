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
import com.handsome.module.login.databinding.FragmentEmailLoginBinding
import com.handsome.module.login.page.viewmodel.LoginViewModel
import com.handsome.module.login.router.LOGIN_EMAIL_ENTER
import com.handsome.module.login.utils.BaseTextWatcher
import com.handsome.module.login.utils.URLUtil
import com.handsome.module.login.utils.ValidityCheckUtil
import com.handsome.module.login.utils.topfuncation.CaptchaLogin
import com.handsome.module.login.utils.topfuncation.EmailLogin
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
@Route(path = LOGIN_EMAIL_ENTER)
class EmailLoginFragment : Fragment() {
    private var _binding: FragmentEmailLoginBinding? = null
    private val binding: FragmentEmailLoginBinding
        get() = _binding!!
    private val model by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailLoginBinding.inflate(inflater, container, false)
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
                model.emailLoginResponseFlow.collectLatest {
                    if (it != null)
                        model.dealEmailLoginResponse(it)
                }
            }
        }
    }

    private fun initEvent() {
        binding.edtEmailToLogin.addTextChangedListener(object : BaseTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                model.isEmailNull(text.isNullOrBlank())
                model.checkClickableLoginByEmail()
            }
        })
        binding.edtPasswordToLogin.addTextChangedListener(object : BaseTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                model.isPasswdNull(text.isNullOrBlank())
                model.checkClickableLoginByEmail()
            }
        })
    }

    private fun initObserve() {
        model.isClickableLoginByEmail.observe(viewLifecycleOwner) {
            binding.btnLoginByEmail.isEnabled = it
        }
    }

    private fun initClick() {
        binding.resetPassword.setOnClickListener {

        }
        binding.btnLoginByEmail.setOnSingleClickListener {
            val email = binding.edtEmailToLogin.text?.toString()
            val password = binding.edtPasswordToLogin.text?.toString()
            if (!ValidityCheckUtil.isValidEmail(email)) {
                toast("请输入正确的网易邮箱地址")
            }else if (password.isNullOrBlank()) toast("请输入密码")
            else {
                // 设定此处为成功发出登录请求的情况
                model.emailLogin(email!!, URLUtil.encodeStringByUTF8(password))
            }
        }
    }

    private fun initView() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbarInLoginByEmail)
        }
        binding.toolbarInLoginByEmail.apply {
            // 点击返回上一个fragment
            setNavigationOnClickListener {
                gotoFragmentPage(
                    this@EmailLoginFragment,
                    CaptchaLogin, EmailLogin
                ) { PhoneCaptchaLoginFragment() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("onDestroyView","Email destroy")
    }
}