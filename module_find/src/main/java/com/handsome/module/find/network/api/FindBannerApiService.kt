package com.handsome.module.find.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.find.network.model.BannerData
import retrofit2.http.GET

interface FindBannerApiService {

    @GET("banner?type=1")
    suspend fun getBanner() : BannerData

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(FindBannerApiService::class)
        }
    }
}