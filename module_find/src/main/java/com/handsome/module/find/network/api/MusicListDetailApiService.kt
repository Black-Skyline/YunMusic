package com.handsome.module.find.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.find.network.model.AlbumData
import com.handsome.module.find.network.model.MusicListDetailData
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicListDetailApiService {

    @GET("playlist/track/all")
    suspend fun getMusicListDetailData(@Query("id") id : Long,@Query("limit") limit : Int,@Query("offset")offset : Int) : MusicListDetailData

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(MusicListDetailApiService::class)
        }
    }
}