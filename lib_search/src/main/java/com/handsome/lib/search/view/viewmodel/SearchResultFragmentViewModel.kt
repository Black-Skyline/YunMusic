package com.handsome.lib.search.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.handsome.lib.search.network.model.SearchResultData
import com.handsome.lib.search.view.adapter.SearchResultPagingSource
import kotlinx.coroutines.flow.Flow

class SearchResultFragmentViewModel : ViewModel() {

    fun searchData(key : String) : Flow<PagingData<SearchResultData.Result.Song>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = { SearchResultPagingSource(key) }
        ).flow
    }
}