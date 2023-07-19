package com.handsome.module.find.network.api

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.find.network.model.AlbumData
import retrofit2.http.GET
import retrofit2.http.Query

interface FindAlbumApiService {

    @GET("album")
    suspend fun getAlbumData(@Query("id") id : Long) : AlbumData

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(FindAlbumApiService::class)
        }
    }
}