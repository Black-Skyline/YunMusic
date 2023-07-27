package com.handsome.module.find.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.module.find.network.api.FindRecommendDetailApiService
import com.handsome.lib.mv.network.exception.myCoroutineExceptionHandler
import com.handsome.module.find.network.model.RecommendDetailData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecommendDetailViewModel : ViewModel() {

    private val _mutableRecommendDetailStateFlow = MutableStateFlow<RecommendDetailData?>(null)
    val recommendDetailStateFlow : StateFlow<RecommendDetailData?>
        get() = _mutableRecommendDetailStateFlow.asStateFlow()

    fun getRecommendDetailData(){
        viewModelScope.launch(myCoroutineExceptionHandler + Dispatchers.IO) {
            _mutableRecommendDetailStateFlow.emit(FindRecommendDetailApiService.INSTANCE.getRecommendDetail())
        }
    }

}