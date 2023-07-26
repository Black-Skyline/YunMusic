package com.handsome.module.mine

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.handsome.lib.music.model.WrapPlayInfo
import kotlinx.coroutines.flow.Flow

const val PAGE_SIZE = 30
class MineFragmentViewModel : ViewModel() {


    fun loadMusic() : Flow<PagingData<WrapPlayInfo>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = {LatestMusicPagingSource()}
        ).flow
    }

}