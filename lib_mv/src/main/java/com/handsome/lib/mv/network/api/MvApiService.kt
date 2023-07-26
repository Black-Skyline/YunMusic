package com.handsome.lib.mv.network.api

import com.handsome.lib.mv.network.model.MvData
import com.handsome.lib.util.network.ApiGenerator
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MvApiService {

    @GET("mv/url")
    fun getMvData(@Query("id") id : Long) : Single<MvData>

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(MvApiService::class)
        }
    }

}