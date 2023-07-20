package com.handsome.module.login.page.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.module.login.model.CaptchaResponseData
import com.handsome.module.login.network.api.CaptchaManageApiService
import com.handsome.module.login.utils.URLUtil.exceptionPrinter
import kotlinx.coroutines.Dispatchers
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
    private var _captchaResponseFlow = MutableStateFlow<CaptchaResponseData?>(null)
    val captchaResponseFlow: StateFlow<CaptchaResponseData?>
        get() = _captchaResponseFlow.asStateFlow()
    fun getCaptcha(phone: Long) {
        viewModelScope.launch (exceptionPrinter + Dispatchers.IO){
            _captchaResponseFlow.emit(CaptchaManageApiService.INSTANCE.getCaptcha(phone))
        }
    }
}