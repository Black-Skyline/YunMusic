package com.handsome.module.login.page.view

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.handsome.lib.util.extention.toast
import com.handsome.module.login.R
import com.handsome.module.login.databinding.FragmentPhoneCaptchaLoginBinding
import com.handsome.module.login.page.viewmodel.LoginViewModel
import com.handsome.module.login.utils.BaseTextWatcher
import com.handsome.module.login.utils.topfuncation.EmailLogin
import com.handsome.module.login.utils.topfuncation.PasswordLogin
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
class PhoneCaptchaLoginFragment : Fragment() {
    private var _binding: FragmentPhoneCaptchaLoginBinding? = null
    private val binding: FragmentPhoneCaptchaLoginBinding
        get() = _binding!!
    private val model by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    private val dialog by lazy { popUserAgreementDialog() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneCaptchaLoginBinding.inflate(inflater, container, false)
        initView()
        initClick()
        initObserve()
        initEvent()
        initSubscribe()
        return binding.root
    }

    private fun initView() {

    }

    private fun initSubscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.captchaResponseFlow.collectLatest {
                    if (it != null)
                        model.dealCaptchaResponse(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.anonymousLoginResponseFlow.collectLatest {
                    if (it != null)
                        model.dealAnonymousLoginResponse(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.captchaLoginResponseFlow.collectLatest {
                    if (it != null)
                        model.dealCaptchaLoginResponse(it)
                }
            }
        }
    }

    private fun initEvent() {
        binding.edtPhoneToLogin.addTextChangedListener(object : BaseTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                model.isPhoneNumber1Null(text.isNullOrBlank())
                model.checkClickableLoginByCaptcha()
            }
        })
        binding.edtCaptchaToLogin.addTextChangedListener(object : BaseTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                model.isCaptchaNull(text.isNullOrBlank())
                model.checkClickableLoginByCaptcha()
            }
        })
        binding.consentClause.setOnCheckedChangeListener { self, state ->
            model.isConsentClause(state)
        }
    }

    private fun initObserve() {
        model.isClickableLoginByCaptcha.observe(viewLifecycleOwner) {
            binding.btnLoginByCaptcha.isEnabled = it
        }
        model.isConsentClause.observe(viewLifecycleOwner) {
            binding.consentClause.isChecked = it
        }
    }

    private fun initClick() {
        // 验证码登录，默认一秒内只响应一次点击事件
        binding.btnLoginByCaptcha.setOnSingleClickListener {
            if (!model.isConsentClause.value!!) {
                dialog.show()
            } else {
                val phoneNumber = binding.edtPhoneToLogin.text?.toString()
                val captcha = binding.edtCaptchaToLogin.text?.toString()
                if (!ValidityCheckUtil.isValidPhoneNumber(phoneNumber)) {
                    toast("请按中国大陆手机号码格式正确输入手机号")
                } else if (!ValidityCheckUtil.isValidCaptcha(captcha)) toast("非法验证码")
                else {
                    // 设定此处为成功发出登录请求的情况

                    model.captchaLogin(phoneNumber!!.toLong(), captcha!!.toInt())
                }
            }
        }

        // 获取验证码的逻辑
        binding.btnGetCaptcha.setOnSingleClickListener { view ->
            // 当手机号码合法时
            val phone = binding.edtPhoneToLogin.text?.toString()
            if (ValidityCheckUtil.isValidPhoneNumber(phone)) {
                // 发起获取验证码请求
                model.getCaptcha(phone!!.toLong())
                // 禁用一分钟的触摸响应，结束后恢复
                var times = 60
                model.delayedUITask(60, before = { view.isEnabled = false }, waiting = {
                    times--
                    (view as Button).text = "剩余${times}秒"
                }, after = {
                    (view as Button).text = "获取验证码"
                    view.isEnabled = true
                })
            } else {
                toast("请正确输入手机号之后再获取验证码")
            }
        }
        // 跳转PhonePasswordLoginFragment
        binding.btnChangeLoginByPassword.setOnSingleClickListener {
            if (!model.isConsentClause.value!!) {
                dialog.show()
            } else {
                gotoFragmentPage(this, PasswordLogin, PasswordLogin) {
                    PhonePasswordLoginFragment()
                }
            }
        }
        // 游客登录
        binding.anonymousEnter.setOnSingleClickListener {
            if (!model.isConsentClause.value!!) {
                dialog.show()
            } else {
                toast("点击了游客登录")
                model.anonymousLogin()
            }
        }
        // 跳转EmailLoginFragment
        binding.loginBtnEmail.setOnSingleClickListener {
            if (!model.isConsentClause.value!!) {
                dialog.show()
            } else {
                gotoFragmentPage(this, EmailLogin, EmailLogin) {
                    EmailLoginFragment()
                }
            }
        }
        // 底部的摆设按钮
        binding.loginBtnQq.setOnClickListener {
            toast("摆设作用，大概要用到ContentProvide，不会写")
        }
        binding.loginBtnWechat.setOnClickListener {
            toast("摆设作用，大概要用到ContentProvide，不会写")
        }
        binding.loginBtnSina.setOnClickListener {
            toast("摆设作用，大概要用到ContentProvide，不会写")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("onDestroyView", "Captcha destroy")
    }

    private fun popUserAgreementDialog(): AlertDialog {
        val view = LayoutInflater.from(requireActivity())
            .inflate(R.layout.alertdialog_bottom_user_agreement, null, false)
        val bottomDialog =
            AlertDialog.Builder(requireActivity(), R.style.BottomUserAgreementDialog).setView(view)
                .create()
        bottomDialog.setCanceledOnTouchOutside(true)

        view.findViewById<Button>(R.id.disagree).setOnClickListener {
            model.isConsentClause(false)
            bottomDialog.dismiss()
        }
        view.findViewById<TextView>(R.id.dialog_content).text =
            "进入下一步之前，请${binding.consentClause.text.substring(2)}"
        view.findViewById<Button>(R.id.agree).setOnClickListener {
            model.isConsentClause(true)
            bottomDialog.dismiss()
        }
        bottomDialog.window?.apply {
            setGravity(Gravity.BOTTOM)
            setLayout(//设置对话框的大小
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            setWindowAnimations(R.style.BottomUserAgreementDialog_Animation)
        }
        return bottomDialog
    }

    fun getCookies() =
        model.internalCookies.value

}