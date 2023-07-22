package com.handsome.module.podcast.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.podcast.model.ProgramAudioData
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
interface ProgramAudioApiService {

    @GET("song/url")
    suspend fun getAudioResponse(@Query("id") id: Long) : ProgramAudioData

    companion object {
        val InSTANCE by lazy {
            ApiGenerator.getApiService(ProgramAudioApiService::class)
        }
    }
}