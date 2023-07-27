package com.handsome.lib.mv.view.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.handsome.lib.mv.network.api.MvRecommendApiService
import com.handsome.lib.mv.network.model.MvRecommendData
import com.handsome.lib.util.extention.toast

class RecommendMvPagingSource : PagingSource<Int,MvRecommendData.Data>() {
    override fun getRefreshKey(state: PagingState<Int, MvRecommendData.Data>): Int?=null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MvRecommendData.Data> {
        return try {
            //先通过params得到参数page，可能为null，如果为null就设置为1
            val page = params.key ?: 1 // set page 1 as default
            //获取每一页包含多少数据
            val pageSize = params.loadSize
            //从服务器获取数据，然后就可以分页了
            val repoResponse = MvRecommendApiService.INSTANCE.getRecommendData(pageSize,pageSize * (page - 1))
            val repoItems = repoResponse.data
            if (repoItems.isEmpty()) "暂无相关推荐".toast()
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (repoItems.isNotEmpty()) page + 1 else null
            LoadResult.Page(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            "网络异常，请重试".toast()
            LoadResult.Error(e)
        }
    }
}