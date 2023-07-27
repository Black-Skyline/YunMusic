package com.handsome.module.podcast.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.podcast.model.RadioProgramsData
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
interface RadioProgramsApiService {

    @GET("dj/program")
    suspend fun getProgramsResponse(
        @Query("rid") rid: Long,
        @Query("limit") limit: Int = 30
    ): RadioProgramsData

    /**
     * 直接获取十个不会更新的节目
     * @return
     */
//    @GET("program/recommend")
//    suspend fun getRecommendProgramResponse() :

    companion object {
        val InSTANCE by lazy {
            ApiGenerator.getApiService(RadioProgramsApiService::class)
        }
    }
}