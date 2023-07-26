package com.handsome.lib.search.network.api

import com.handsome.lib.search.network.model.SearchResultData
import com.handsome.lib.util.network.ApiGenerator
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchResultApiService {

    @GET("search")
    suspend fun getSearchResult(@Query("keywords") keywords : String , @Query("limit") limit : Int, @Query("offset") offset : Int) : SearchResultData

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(SearchResultApiService::class)
        }
    }
}