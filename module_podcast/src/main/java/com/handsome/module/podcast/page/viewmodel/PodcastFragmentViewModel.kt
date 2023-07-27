package com.handsome.module.podcast.page.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.module.podcast.model.FMProgramsData
import com.handsome.module.podcast.model.NormalRecommendationData
import com.handsome.module.podcast.model.PersonalizeRecommendationData
import com.handsome.module.podcast.model.RadioProgramsData
import com.handsome.module.podcast.network.api.RadioProgramsApiService
import com.handsome.module.podcast.network.api.RadioStationRecommendationApiService
import com.handsome.module.podcast.utils.exceptionPrinter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ...
 * @author: Black-skyline (Hu Shujun)
 * @email: 2031649401@qq.com
 * @date: 2023/7/21
 * @Description:
 *
 */
class PodcastFragmentViewModel : ViewModel() {
    /**
     * 获取个性化(兴趣)推荐 response流的容器
     */
    private var _personalizeRecommendResponseFlow =
        MutableStateFlow<PersonalizeRecommendationData?>(null)
    val personalizeRecommendResponseFlow: StateFlow<PersonalizeRecommendationData?> get() = _personalizeRecommendResponseFlow.asStateFlow()

    /**
     * 获取常规推荐 response流的容器
     */
    private var _normalRecommendResponseFlow = MutableStateFlow<NormalRecommendationData?>(null)
    val normalRecommendResponseFlow: StateFlow<NormalRecommendationData?> get() = _normalRecommendResponseFlow.asStateFlow()

    /**
     * 获取FMPrograms流的容器
     */
    private var _fmProgramsResponseFlow = MutableStateFlow<FMProgramsData?>(null)
    val fmProgramsResponseFlow: StateFlow<FMProgramsData?> get() = _fmProgramsResponseFlow.asStateFlow()

    /**
     * 获取FMPrograms流的容器
     */
    private var _ProgramsResponseFlow = MutableStateFlow<RadioProgramsData?>(null)
    val ProgramsResponseFlow: StateFlow<RadioProgramsData?> get() = _ProgramsResponseFlow.asStateFlow()

    /**
     * 向网络获取个性化推荐数据
     * @param size  想得到的电台数据个数
     */
    fun getPersonalizeRecommend(size: Int = 6) {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _personalizeRecommendResponseFlow.emit(RadioStationRecommendationApiService.InSTANCE.getInterestRecommendationResponse(size)
            )
        }
    }

    /**
     * 向网络获取常规推荐数据
     */
    fun getNormalRecommend() {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _normalRecommendResponseFlow.emit(
                RadioStationRecommendationApiService.InSTANCE.getNormalRecommendationResponse()
            )
        }
    }

    fun getFMPrograms() {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _fmProgramsResponseFlow.emit(
                RadioProgramsApiService.InSTANCE.getFMProgramsResponse()
            )
        }
    }

    fun getPrograms() {
        viewModelScope.launch(exceptionPrinter + Dispatchers.IO) {
            _fmProgramsResponseFlow.emit(
                RadioProgramsApiService.InSTANCE.getFMProgramsResponse()
            )
        }
    }
}