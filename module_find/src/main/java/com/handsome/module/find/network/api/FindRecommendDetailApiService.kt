package com.handsome.module.find.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.find.network.model.RecommendDetailData
import retrofit2.http.GET
import retrofit2.http.Query

interface FindRecommendDetailApiService {

    @GET("recommend/songs")
    suspend fun getRecommendDetail(@Query("timestamp") timestamp : Long = 1) : RecommendDetailData

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(FindRecommendDetailApiService::class)
        }
    }
}