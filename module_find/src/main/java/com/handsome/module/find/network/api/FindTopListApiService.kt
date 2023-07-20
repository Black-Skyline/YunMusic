package com.handsome.module.find.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.find.network.model.TopListData
import retrofit2.http.GET

interface FindTopListApiService {

    @GET("toplist/detail")
    suspend fun getTopList() : TopListData


//    @GET("playlist/track/all")
//    suspend fun getTopListDetail(@Query("id")id : Long,@Query("limit")limit : Int = 10,@Query("offset")offset : Int = 1) : MusicListDetail


    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(FindTopListApiService::class)
        }
    }
}