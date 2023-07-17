package com.handsome.module.find.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.find.network.model.RecommendMusicListData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FindRecommendListApiService {

    @GET("personalized")
    suspend fun getRecommendList(@Query("limit") size : Int) : RecommendMusicListData

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(FindRecommendListApiService::class)
        }
    }
}