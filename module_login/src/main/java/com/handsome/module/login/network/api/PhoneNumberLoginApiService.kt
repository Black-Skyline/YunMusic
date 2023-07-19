package com.handsome.module.login.network.api

import com.handsome.lib.util.network.ApiGenerator
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
interface PhoneNumberLoginApiService {

    @GET("login/cellphone?phone={phone}&password={password}")
    suspend fun getResponseByPassword(@Path("phone") phone: Long, @Path("password") password: String)

    @GET("login/cellphone?phone={phone}&captcha={captcha}")
    suspend fun getResponseByCaptcha(@Path("phone") phone: Long, @Path("captcha") captcha: Int)

    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(PhoneNumberLoginApiService::class)
        }
    }
}