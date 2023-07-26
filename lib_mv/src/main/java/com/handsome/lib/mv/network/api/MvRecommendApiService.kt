package com.handsome.lib.mv.network.api

import com.handsome.lib.mv.network.model.MvRecommendData
import com.handsome.lib.util.network.ApiGenerator
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MvRecommendApiService {

    @GET("mv/all")
    fun getRecommendData() : Single<MvRecommendData>

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(MvRecommendApiService::class)
        }
    }
}