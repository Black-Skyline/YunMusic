package com.handsome.module.podcast.page.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.handsome.module.podcast.model.RadioProgramsData
import com.handsome.module.podcast.model.RadioStationDetail
import com.handsome.module.podcast.network.api.RadioProgramsApiService
import com.handsome.module.podcast.page.adapter.ProgramsDisplayPagingSource
import com.handsome.module.podcast.utils.exceptionPrinter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/27
 * @Description:
 *
 */
class ProgramsListDetailViewModel : ViewModel() {

    val PAGE_SIZE  = 10

    /**
     * 节目列表response流容器
     */
    private var _programsListResponseFlow = MutableStateFlow<RadioProgramsData?>(null)
    val programsListResponseFlow: StateFlow<RadioProgramsData?> get() = _programsListResponseFlow.asStateFlow()

    /**
     * 电台详情response流容器
     */
    private var _radioDetailResponseFlow = MutableStateFlow<RadioStationDetail?>(null)
    val radioDetailResponseFlow: StateFlow<RadioStationDetail?> get() = _radioDetailResponseFlow.asStateFlow()

    /**
     * 给定指定电台rid，获取其所有的节目列表
     * @param rid
     * 其 limit 、offset均由paging决定
     */
    fun getProgramsList(rid: Long) : Flow<PagingData<RadioProgramsData.Program>> {
        return Pager(
            pagingSourceFactory = {ProgramsDisplayPagingSource(rid)},
            config = PagingConfig(PAGE_SIZE)
        ).flow
    }

    /**
     * 获取电台详情数据
     * @param rid
     */
    fun getRadioDetail(rid: Long) {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _radioDetailResponseFlow.emit(
                RadioProgramsApiService.InSTANCE.getRadioDetailResponse(rid)
            )
        }
    }
}