package com.handsome.module.login.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.login.model.PhoneCaptchaData
import com.handsome.module.login.model.PhonePasswordData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/19
 * @Description:
 *
 */
interface PhoneNumberLoginApiService {

    @GET("login/cellphone")
    suspend fun getResponseByPassword(@Query("phone") phone: Long, @Query("password") password: String) : PhonePasswordData

    @GET("login/cellphone")
    suspend fun getResponseByCaptcha(@Query("phone") phone: Long, @Query("captcha") captcha: Int) : PhoneCaptchaData

    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(PhoneNumberLoginApiService::class)
        }
    }
}