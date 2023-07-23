package com.handsome.module.find.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.handsome.module.find.network.api.SingleMusicListDetailApiService
import com.handsome.module.find.network.exception.myCoroutineExceptionHandler
import com.handsome.module.find.network.model.MusicListDetailData
import com.handsome.module.find.network.model.SingleMusicListDetailData
import com.handsome.module.find.view.adapter.MusicListDetailPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val PAGE_SIZE  = 10

class MusicListDetailViewModel : ViewModel() {

    private val _mutableMusicListStateFlow = MutableStateFlow<MusicListDetailData?>(null)
    val stateFlow : StateFlow<MusicListDetailData?>
        get() = _mutableMusicListStateFlow.asStateFlow()

    private val _mutableSingleMusicListDetailStateFlow = MutableStateFlow<SingleMusicListDetailData?>(null)
    val singleMusicListDetailStateFlow : StateFlow<SingleMusicListDetailData?>
        get() = _mutableSingleMusicListDetailStateFlow.asStateFlow()

    fun getMusicListData(id : Long) : Flow<PagingData<MusicListDetailData.Song>>{
        return Pager(
            pagingSourceFactory = {MusicListDetailPagingSource(id)},
            config = PagingConfig(PAGE_SIZE)
        ).flow
    }

    fun getSingleMusicDetailData(id : Long){
        viewModelScope.launch(myCoroutineExceptionHandler + Dispatchers.IO) {
            _mutableSingleMusicListDetailStateFlow.emit(SingleMusicListDetailApiService.INSTANCE.getSingleMusicListDetail(id))
        }
    }
}