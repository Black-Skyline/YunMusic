package com.handsome.module.podcast.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.podcast.model.NormalRecommendationData
import com.handsome.module.podcast.model.RadioStationRecommendationData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/22
 * @Description:
 *
 */
interface RadioStationRecommendationApiService {

    // limit 最大为6
    @GET("dj/personalize/recommend")
    suspend fun getRecommendationResponse(@Query("limit") limit: Int = 6) : RadioStationRecommendationData

    @GET("dj/recommend")
    suspend fun getNormalRecommendationResponse() : NormalRecommendationData

    companion object {
        val InSTANCE by lazy {
            ApiGenerator.getApiService(RadioStationRecommendationApiService::class)
        }
    }
}