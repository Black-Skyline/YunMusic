package com.handsome.module.find.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.find.network.model.SingleMusicListDetailData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 获取单一歌单的详细信息，包括描述
 */
interface SingleMusicListDetailApiService {

    @GET("playlist/detail")
    suspend fun getSingleMusicListDetail(@Query("id") id : Long) : SingleMusicListDetailData

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(SingleMusicListDetailApiService::class)
        }
    }
}