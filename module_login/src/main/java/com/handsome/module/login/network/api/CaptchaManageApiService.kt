package com.handsome.module.login.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.login.model.CaptchaResponseData
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/19
 * @Description:
 *
 */
interface CaptchaManageApiService {
    @GET("captcha/sent?phone={phone}")
    suspend fun getCaptcha(@Path("phone") phone: Long): CaptchaResponseData

    @GET("captcha/verify?phone={phone}&captcha={captcha}")
    suspend fun verifyCaptcha(
        @Path("phone") phone: Long,
        @Path("captcha") captcha: Int
    ): CaptchaResponseData


    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(CaptchaManageApiService::class)
        }
    }
}