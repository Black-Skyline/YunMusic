package com.handsome.module.login.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.login.model.EmailData
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
interface EmailLoginApiService {

    @GET("/login?email={email}&password={password}")
    suspend fun getResponse(
        @Path("email") email: String,
        @Path("password") password: String
    ): EmailData

    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(EmailLoginApiService::class)
        }
    }
}