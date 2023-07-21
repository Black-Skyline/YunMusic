package com.handsome.lib.search.view.adapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.handsome.lib.search.network.api.SearchResultApiService
import com.handsome.lib.search.network.model.SearchResultData

class SearchResultPagingSource(private val key : String) : PagingSource<Int,SearchResultData.Result.Song>() {
    override fun getRefreshKey(state: PagingState<Int, SearchResultData.Result.Song>): Int?=null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResultData.Result.Song> {
        return try {
            val page = params.key ?: 1
            Log.d("lx", "page = ${page}: ")
            val pageSize = params.loadSize
            Log.d("lx", "pageSize = ${pageSize}: ")
            val response = SearchResultApiService.INSTANCE.getSearchResult(key,pageSize,page*pageSize)
            val repoItems = response.result.songs
            val prevKey = if(page > 1) page - 1 else null
            val nextKey = if(repoItems.isNotEmpty()) page + 1 else null
            LoadResult.Page(repoItems,prevKey,nextKey)
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}