package com.handsome.module.podcast.page.adapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.handsome.module.podcast.model.RadioProgramsData
import com.handsome.module.podcast.network.api.RadioProgramsApiService

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/28
 * @Description:
 *
 */
class ProgramsDisplayPagingSource(val rid: Long): PagingSource<Int, RadioProgramsData.Program>() {
    override fun getRefreshKey(state: PagingState<Int, RadioProgramsData.Program>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RadioProgramsData.Program> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
//            Log.d("dataTest", "page=${page} pageSize=$pageSize ")
            val response = RadioProgramsApiService.InSTANCE.getProgramsResponse(rid, pageSize, (page-1) * pageSize)
            val repoItems = response.programs
            val prevKey = if(page > 1) page - 1 else null
            val nextKey = if(repoItems.isNotEmpty()) page + 1 else null
//            Log.d("dataTest", "prevKey = ${prevKey} nextKey = ${nextKey}")
            LoadResult.Page(repoItems,prevKey,nextKey)
        } catch (e:Exception) {
//            Log.d("LogicTest","${e.message}")
            LoadResult.Error(e)
        }
    }

}