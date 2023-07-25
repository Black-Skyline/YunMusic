package com.handsome.module.mine

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.music.room.AppDatabase

class LatestMusicPagingSource : PagingSource<Int, WrapPlayInfo>() {
    override fun getRefreshKey(state: PagingState<Int, WrapPlayInfo>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WrapPlayInfo> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            Log.d("lx", "page=${page} pageSize=$pageSize ")
            val response = AppDatabase.getDataBase().wrapPlayInfoDao().loadAllMusic((page-1) * pageSize,pageSize)
            val prevKey = if(page > 1) page - 1 else null
            val nextKey = if (response.isNotEmpty()) page + 1 else null
            Log.d("", "prevKey = $prevKey nextKey = $nextKey")
            LoadResult.Page(response, prevKey, nextKey)
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}