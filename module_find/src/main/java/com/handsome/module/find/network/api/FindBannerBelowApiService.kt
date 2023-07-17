package com.handsome.module.find.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.find.network.model.BannerBelowData
import retrofit2.http.GET

interface FindBannerBelowApiService {

    @GET("homepage/dragon/ball")
    suspend fun getBannerBelow() : BannerBelowData

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(FindBannerBelowApiService::class)
        }
    }
}