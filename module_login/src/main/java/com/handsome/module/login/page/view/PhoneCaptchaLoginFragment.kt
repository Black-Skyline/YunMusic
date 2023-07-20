package com.handsome.module.login.page.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.handsome.lib.util.extention.toast
import com.handsome.module.login.databinding.FragmentPhoneCaptchaLoginBinding
import com.handsome.module.login.page.viewmodel.LoginViewModel
import com.handsome.module.login.utils.topfuncation.ValidityCheckUtil
import com.handsome.module.login.utils.topfuncation.setOnSingleClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/19
 * @Description:
 *
 */
class PhoneCaptchaLoginFragment : Fragment() {
    private var _binding: FragmentPhoneCaptchaLoginBinding? = null
    private val binding: FragmentPhoneCaptchaLoginBinding
        get() = _binding!!
    private val model by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneCaptchaLoginBinding.inflate(inflater, container, false)
        initView()
        initClick()
        initEvent()
        initObserve()
        return binding.root
    }

    private fun initView() {

    }

    private fun initObserve() {

    }

    private fun initEvent() {

    }

    private fun initClick() {
        // 默认一秒内只响应一次点击事件
        binding.loginBtnByCaptcha.setOnSingleClickListener {
            val phoneNumber = binding.loginEtPhone.text?.toString()
            val captcha = binding.loginEtCaptcha.text?.toString()
            if (!ValidityCheckUtil.isValidPhoneNumber(phoneNumber)) {
                toast("请按中国大陆手机号码格式正确输入手机号")
            } else if (!ValidityCheckUtil.isValidCaptcha(captcha))
                toast("非法验证码")
            else {
                // 设定此处为成功登录情况
            }
        }
        binding.btGetCaptcha.setOnSingleClickListener {
            it.isEnabled = false
            // 禁用一分钟的触摸响应，结束后恢复
            CoroutineScope(Job()).launch {
                repeat(60) {
                    delay(1000)
                }
                it.isEnabled = true
            }
            // 当号码合法时
            if (true) {
                // 发起获取验证码请求, 以123为例
                model.getCaptcha(123)
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}