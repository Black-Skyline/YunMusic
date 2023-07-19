package com.handsome.module.login.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.login.model.AnonymousData
import retrofit2.http.GET

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/19
 * @Description:
 *
 */
interface AnonymousLoginApiService {

    @GET("register/anonimous")
    suspend fun getResponse() : AnonymousData

    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(AnonymousLoginApiService::class)
        }
    }
}