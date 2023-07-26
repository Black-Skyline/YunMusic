package com.handsome.lib.music.network.api

import com.handsome.lib.music.model.AudioUrlData
import com.handsome.lib.util.network.ApiGenerator
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/23
 * @Description:
 *
 */
interface AudioUrlApiService {

    @GET("song/url/v1")
    fun getSingleUrlResponse(
        @Query("id") id: Long,
        @Query("level") level: String = "standard"
    ): Single<List<AudioUrlData>>

    @GET("song/url/v1")
    fun getMultipleUrlResponse(
        @Query("id") multiId: String,
        @Query("level") level: String = "standard"
    ): Single<AudioUrlData>

    companion object {
        val INSTANCE by lazy { ApiGenerator.getApiService(AudioUrlApiService::class) }
    }
}