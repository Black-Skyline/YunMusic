package com.handsome.module.find.view.adapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.handsome.module.find.network.api.MusicListDetailApiService
import com.handsome.module.find.network.model.MusicListDetailData

class MusicListDetailPagingSource(val id:Long) : PagingSource<Int,MusicListDetailData.Song>() {
    override fun getRefreshKey(state: PagingState<Int, MusicListDetailData.Song>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MusicListDetailData.Song> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            Log.d("lx", "page=${page} pageSize=$pageSize ")
            val response = MusicListDetailApiService.INSTANCE.getMusicListDetailData(id,pageSize,(page-1) * pageSize)
            val repoItems = response.songs
            val prevKey = if(page > 1) page - 1 else null
            val nextKey = if(repoItems.isNotEmpty()) page + 1 else null
            Log.d("", "prevKey = ${prevKey} nextKey = ${nextKey}")
            LoadResult.Page(repoItems,prevKey,nextKey)
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}