package com.handsome.module.login.page.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.extention.toast
import com.handsome.module.login.model.AnonymousData
import com.handsome.module.login.model.CaptchaResponseData
import com.handsome.module.login.model.EmailData
import com.handsome.module.login.model.PhoneCaptchaData
import com.handsome.module.login.model.PhonePasswordData
import com.handsome.module.login.network.api.AnonymousLoginApiService
import com.handsome.module.login.network.api.CaptchaManageApiService
import com.handsome.module.login.network.api.EmailLoginApiService
import com.handsome.module.login.network.api.PhoneNumberLoginApiService
import com.handsome.module.login.utils.URLUtil.exceptionPrinter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/19
 * @Description:
 *
 */
class LoginViewModel : ViewModel() {
    private val cookies = MutableLiveData<String>()
    val internalCookies: LiveData<String> get() = cookies

    /**
     * 验证码登录按钮的可点击性状态
     */
    private val _isClickableLoginByCaptcha = MutableLiveData(false)
    val isClickableLoginByCaptcha: LiveData<Boolean> get() = _isClickableLoginByCaptcha.distinctUntilChanged()

    /**
     * 手机号密码登录按钮的可点击性状态
     */
    private val _isClickableLoginByPassword = MutableLiveData(false)
    val isClickableLoginByPassword: LiveData<Boolean> get() = _isClickableLoginByPassword.distinctUntilChanged()

    /**
     * 邮箱登录按钮的可点击性状态
     */
    private val _isClickableLoginByEmail = MutableLiveData(false)
    val isClickableLoginByEmail: LiveData<Boolean> get() = _isClickableLoginByEmail.distinctUntilChanged()

    /**
     * 用户须知的同意状态
     */
    private val _isConsentClause = MutableLiveData(false)
    val isConsentClause: LiveData<Boolean> get() = _isConsentClause.distinctUntilChanged()

    /**
     * 验证码登录界面 编辑框判空状态位
     */
    private var isPhoneNumber1Null: Boolean = true
    private var isCaptchaNull: Boolean = true

    /**
     * 手机号密码登录界面 编辑框判空状态位
     */
    private var isPhoneNumber2Null: Boolean = true
    private var isPasswordNull: Boolean = true

    /**
     * 邮箱密码登录界面 编辑框判空状态位
     */
    private var isEmailNull: Boolean = true
    private var isPasswdNull: Boolean = true

    /**
     * 获取验证码response流的容器
     */
    private var _captchaResponseFlow = MutableStateFlow<CaptchaResponseData?>(null)
    val captchaResponseFlow: StateFlow<CaptchaResponseData?> get() = _captchaResponseFlow.asStateFlow()

    /**
     * 获取验证码登录response流的容器
     */
    private var _captchaLoginResponseFlow = MutableStateFlow<PhoneCaptchaData?>(null)
    val captchaLoginResponseFlow: StateFlow<PhoneCaptchaData?> get() = _captchaLoginResponseFlow.asStateFlow()

    /**
     * 获取匿名登录response流的容器
     */
    private var _anonymousLoginResponseFlow = MutableStateFlow<AnonymousData?>(null)
    val anonymousLoginResponseFlow: StateFlow<AnonymousData?> get() = _anonymousLoginResponseFlow.asStateFlow()

    /**
     * 获取手机号密码登录response流的容器
     */
    private var _passwordLoginResponseFlow = MutableStateFlow<PhonePasswordData?>(null)
    val passwordLoginResponseFlow: StateFlow<PhonePasswordData?> get() = _passwordLoginResponseFlow.asStateFlow()

    /**
     * 获取网易云邮箱登录response流的容器
     */
    private var _emailLoginResponseFlow = MutableStateFlow<EmailData?>(null)
    val emailLoginResponseFlow: StateFlow<EmailData?> get() = _emailLoginResponseFlow.asStateFlow()

    /**
     * 获取验证码的方法
     * @param phone 合法的号码值
     */
    fun getCaptcha(phone: Long) {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _captchaResponseFlow.emit(CaptchaManageApiService.INSTANCE.getCaptcha(phone))
        }
    }

    fun verifyCaptcha(phone: Long, captcha: Int) {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _captchaResponseFlow.emit(CaptchaManageApiService.INSTANCE.getCaptcha(phone))
        }
    }

    /**
     * 匿名登录（游客模式）方法
     */
    fun anonymousLogin() {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _anonymousLoginResponseFlow.emit(AnonymousLoginApiService.INSTANCE.getResponse())
        }
    }

    /**
     * 验证码登录方法
     *
     * @param phone
     * @param captcha
     */
    fun captchaLogin(phone: Long, captcha: Int) {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _captchaLoginResponseFlow.emit(
                PhoneNumberLoginApiService.INSTANCE.getResponseByCaptcha(
                    phone,
                    captcha
                )
            )
        }
    }

    /**
     * 邮箱密码登录方法
     *
     * @param email
     * @param password
     */
    fun emailLogin(email: String, password: String) {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _emailLoginResponseFlow.emit(EmailLoginApiService.INSTANCE.getResponse(email, password))
        }

    }

    /**
     * 手机号密码登录方法
     *
     * @param phone
     * @param password
     */
    fun passwordLogin(phone: Long, password: String) {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _passwordLoginResponseFlow.emit(
                PhoneNumberLoginApiService.INSTANCE.getResponseByPassword(
                    phone,
                    password
                )
            )
        }
    }

    /**
     * 处理获取验证码收到的response
     * @param response
     */
    fun dealCaptchaResponse(response: CaptchaResponseData?, action: (() -> Unit)? = null) {
        if (response != null && response.code == 200 && response.data) toast("验证码请求成功")
        else toast("验证码请求失败")
    }

    /**
     * 处理校验验证码收到的response
     * @param response
     */
    fun dealCaptchaVerifyResponse(response: CaptchaResponseData?, action: (() -> Unit)? = null) {
        if (response != null && response.code == 200 && response.data) {
            toast("验证码校验成功")
            action?.invoke()
        } else toast("验证码校验失败")
    }

    /**
     * 处理匿名登录（游客模式）收到的response
     * @param response
     */
    fun dealAnonymousLoginResponse(
        response: AnonymousData?,
        action: ((state: Boolean) -> Unit)? = null
    ) {
        if (response != null && response.code == 200) {
            cookies.value = response.cookie
            action?.invoke(true)

        } else {
            toast("匿名登录失败")
            action?.invoke(false)
        }
    }

    fun dealPasswordLoginResponse(response: PhonePasswordData?, action: (() -> Unit)? = null) {
        if (response != null && response.code == 200) {
            toast("密码 登录成功")
            action?.invoke()
        } else toast("密码 登录失败")
    }


    fun dealCaptchaLoginResponse(response: PhoneCaptchaData?, action: (() -> Unit)? = null) {
//        if (response != null && response.code == 200) {
//            Log.d("dealCaptchaLoginResponse", "code is ${response.code}")
//            toast("验证码 登录成功")
        Log.d("tag", "get there")
        action?.invoke()
//        } else toast("验证码登录失败")
    }

    fun dealEmailLoginResponse(response: EmailData?, action: (() -> Unit)? = null) {
        if (response != null && response.code == 200) {
            toast("邮箱 登录成功")
            action?.invoke()
        } else toast("邮箱登录失败")
    }


    /**
     * 在主线程开一个携程，可指定一定的等待时间，可在等待前、等待时、等待后分别执行UI任务
     *
     * @param times    指定的等待时间，单位  s
     * @param before    等待前可执行的任务
     * @param waiting   等待时可执行的任务，频率为每秒一次
     * @param after     等待后可执行的任务
     */
    fun delayedUITask(
        times: Int,
        before: (() -> Unit)? = null,
        waiting: (() -> Unit)? = null,
        after: (() -> Unit)? = null
    ) {
        // 禁用一分钟的触摸响应，结束后恢复
        CoroutineScope(Dispatchers.Main).launch {
            before?.invoke()
            repeat(times) {
                waiting?.invoke()
                delay(1000)
            }
            after?.invoke()
        }
    }

    fun checkClickableLoginByCaptcha() {
        _isClickableLoginByCaptcha.value = !(isPhoneNumber1Null || isCaptchaNull)
    }

    fun checkClickableLoginByPassword() {
        _isClickableLoginByPassword.value = !(isPhoneNumber2Null || isPasswordNull)
    }

    fun checkClickableLoginByEmail() {
        _isClickableLoginByEmail.value = !(isEmailNull || isPasswdNull)
    }

    fun isConsentClause(switch: Boolean) {
        _isConsentClause.value = switch
    }

    fun isPhoneNumber1Null(switch: Boolean) {
        isPhoneNumber1Null = switch
    }

    fun isCaptchaNull(switch: Boolean) {
        isCaptchaNull = switch
    }

    fun isPhoneNumber2Null(switch: Boolean) {
        isPhoneNumber2Null = switch
    }

    fun isPasswordNull(switch: Boolean) {
        isPasswordNull = switch
    }

    fun isEmailNull(switch: Boolean) {
        isEmailNull = switch
    }

    fun isPasswdNull(switch: Boolean) {
        isPasswdNull = switch
    }
}