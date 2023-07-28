package com.handsome.module.podcast.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.podcast.model.FMProgramsData
import com.handsome.module.podcast.model.RadioProgramsData
import com.handsome.module.podcast.model.RadioStationDetail
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


    /**
     * 获取每个电台的节目列表
     * @param rid
     * @param limit
     * @param offset
     * @param asc
     * @return
     */
    @GET("dj/program")
    suspend fun getProgramsResponse(
        @Query("rid") rid: Long,
        @Query("limit") limit: Int = 30,
        @Query("offset") offset: Int = 0,
        @Query("asc") asc: Boolean = false
    ): RadioProgramsData


    /**
     * 直接获取十个不会更新的节目
     * @return
     */
    @GET("program/recommend")
    suspend fun getFMProgramsResponse(): FMProgramsData

    @GET("dj/detail")
    suspend fun getRadioDetailResponse(@Query("rid") rid: Long) : RadioStationDetail

    companion object {
        val InSTANCE by lazy {
            ApiGenerator.getApiService(RadioProgramsApiService::class)
        }
    }
}