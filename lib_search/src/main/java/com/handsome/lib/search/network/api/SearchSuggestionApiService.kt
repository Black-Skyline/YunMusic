package com.handsome.lib.search.network.api

import com.handsome.lib.search.network.model.SearchSuggestionData
import com.handsome.lib.util.network.ApiGenerator
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchSuggestionApiService {

    @GET("search/suggest")
    suspend fun getSuggestionData(@Query("keywords") keywords : String,@Query("type") type : String = "mobile") : SearchSuggestionData

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(SearchSuggestionApiService::class)
        }
    }
}