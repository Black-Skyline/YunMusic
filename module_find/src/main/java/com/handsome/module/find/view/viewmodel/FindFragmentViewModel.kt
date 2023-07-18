package com.handsome.module.find.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.module.find.network.api.FindBannerApiService
import com.handsome.module.find.network.api.FindBannerBelowApiService
import com.handsome.module.find.network.api.FindRecommendListApiService
import com.handsome.module.find.network.exception.myCoroutineExceptionHandler
import com.handsome.module.find.network.model.BannerBelowData
import com.handsome.module.find.network.model.BannerData
import com.handsome.module.find.network.model.RecommendMusicListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FindFragmentViewModel : ViewModel() {
    private var _mutableBannerStateFlow = MutableStateFlow<BannerData?>(null)
    val bannerStateFlow : StateFlow<BannerData?>
        get() = _mutableBannerStateFlow.asStateFlow()


    private var _mutableBannerBelowStateFlow = MutableStateFlow<BannerBelowData?>(null)
    val bannerBelowStateFlow : StateFlow<BannerBelowData?>
        get() = _mutableBannerBelowStateFlow.asStateFlow()

    private var _mutableRecommendListStateFlow = MutableStateFlow<RecommendMusicListData?>(null)
    val recommendListStateFlow : StateFlow<RecommendMusicListData?>
        get() = _mutableRecommendListStateFlow.asStateFlow()

    fun getBannerData(){
        viewModelScope.launch(myCoroutineExceptionHandler + Dispatchers.IO) {
            _mutableBannerStateFlow.emit(FindBannerApiService.INSTANCE.getBanner())
        }
    }

    fun getBannerBelowData(){
        viewModelScope.launch(myCoroutineExceptionHandler + Dispatchers.IO) {
            _mutableBannerBelowStateFlow.emit(FindBannerBelowApiService.INSTANCE.getBannerBelow())
        }
    }

    fun getRecommendListData(size : Int){
        viewModelScope.launch(myCoroutineExceptionHandler + Dispatchers.IO) {
            _mutableRecommendListStateFlow.emit(FindRecommendListApiService.INSTANCE.getRecommendList(size))
        }
    }
}