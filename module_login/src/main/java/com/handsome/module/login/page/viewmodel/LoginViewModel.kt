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
import com.handsome.module.login.network.api.AnonymousLoginApiService
import com.handsome.module.login.network.api.CaptchaManageApiService
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
    private val _isClickableLoginByCaptcha = MutableLiveData(false)
    val isClickableLoginByCaptcha: LiveData<Boolean> get() = _isClickableLoginByCaptcha.distinctUntilChanged()

    private val _isConsentClause = MutableLiveData(false)
    val isConsentClause: LiveData<Boolean> get() = _isConsentClause.distinctUntilChanged()

    /**
     * 编辑框判空状态位
     */
    private var isPhoneNumberNull: Boolean = true
    private var isCaptchaNull: Boolean = true


    //    private val _phoneNumber = MutableLiveData<String?>()
//    val phoneNumber: LiveData<String?> get() = _phoneNumber.distinctUntilChanged()
//
//    private val _captcha = MutableLiveData<String?>()
//    val captcha: LiveData<String?> get() = _captcha.distinctUntilChanged()
    private var _captchaResponseFlow = MutableStateFlow<CaptchaResponseData?>(null)
    val captchaResponseFlow: StateFlow<CaptchaResponseData?>
        get() = _captchaResponseFlow.asStateFlow()

    private var _anonymousLoginResponseFlow = MutableStateFlow<AnonymousData?>(null)
    val anonymousLoginResponseFlow: StateFlow<AnonymousData?>
        get() = _anonymousLoginResponseFlow.asStateFlow()

    fun getCaptcha(phone: Long) {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _captchaResponseFlow.emit(CaptchaManageApiService.INSTANCE.getCaptcha(phone))
        }
    }

    fun anonymousLogin() {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _anonymousLoginResponseFlow.emit(AnonymousLoginApiService.INSTANCE.getResponse())
        }
    }

    /**
     * 处理获取验证码收到的response
     * @param response
     */
    fun dealCaptchaResponse(response: CaptchaResponseData?) {
        if (response != null && response.code == 200 && response.data) toast("验证码请求成功")
        else if (response == null) toast("验证码请求失败")
    }

    /**
     * 处理匿名登录（游客模式）收到的response
     * @param response
     */
    fun dealAnonymousLoginResponse(response: AnonymousData?) {
        if (response != null && response.code == 200) {
            toast("游客${response.userId} 登录成功")
            // 下面处理cookie和useId
        } else toast("验证码请求失败")
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
//            var times = 60
            repeat(times) {
                waiting?.invoke()
//                times--
//                (view as T).text = "剩余${times}秒"
                delay(1000)
            }
            after?.invoke()
//            (view as Button).text = "获取验证码"
//            view.isEnabled = true
        }
    }

    fun checkClickableLoginByCaptcha() {
        _isClickableLoginByCaptcha.value = !(isPhoneNumberNull || isCaptchaNull)
    }

    fun isConsentClause(switch: Boolean) {
        _isConsentClause.value = switch
    }

    fun isPhoneNumberNull(switch: Boolean) {
        isPhoneNumberNull = switch
    }

    fun isCaptchaNull(switch: Boolean) {
        isCaptchaNull = switch
    }
}