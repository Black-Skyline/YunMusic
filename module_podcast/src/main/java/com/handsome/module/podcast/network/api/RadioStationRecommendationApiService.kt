package com.handsome.module.podcast.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.podcast.model.NormalRecommendationData
import com.handsome.module.podcast.model.PersonalizeRecommendationData
import io.reactivex.rxjava3.core.Single
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
    suspend fun getInterestRecommendationResponse(@Query("limit") limit: Int = 6) : PersonalizeRecommendationData

    @GET("dj/recommend") // 登陆后再调用，不然不会更新
    suspend fun getNormalRecommendationResponse() : NormalRecommendationData
//    personalized/djprogram
//    dj/hot // 热门电台 limit返回数量 , 默认为 30
    companion object {
        val InSTANCE by lazy {
            ApiGenerator.getApiService(RadioStationRecommendationApiService::class)
        }
    }
}