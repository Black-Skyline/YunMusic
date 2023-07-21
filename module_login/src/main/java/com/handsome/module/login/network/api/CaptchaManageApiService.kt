package com.handsome.module.login.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.login.model.CaptchaResponseData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/19
 * @Description:
 *
 */
interface CaptchaManageApiService {
    @GET("captcha/sent")
    suspend fun getCaptcha(@Query("phone") phone: Long): CaptchaResponseData

    @GET("captcha/verify")
    suspend fun verifyCaptcha(
        @Query("phone") phone: Long,
        @Query("captcha") captcha: Int
    ): CaptchaResponseData


    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(CaptchaManageApiService::class)
        }
    }
}