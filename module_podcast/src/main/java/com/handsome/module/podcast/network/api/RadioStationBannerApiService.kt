package com.handsome.module.podcast.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.podcast.model.RadioStationBannerData
import retrofit2.http.GET

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/21
 * @Description:
 *
 */
interface RadioStationBannerApiService {
    @GET("dj/banner")
    suspend fun getBannerResponse() : RadioStationBannerData

    companion object {
        val InSTANCE by lazy {
            ApiGenerator.getApiService(RadioStationBannerApiService::class)
        }
    }
}