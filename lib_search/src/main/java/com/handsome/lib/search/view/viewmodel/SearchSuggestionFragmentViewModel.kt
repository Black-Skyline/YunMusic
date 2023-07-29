package com.handsome.lib.search.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.search.network.api.SearchSuggestionApiService
import com.handsome.lib.search.network.model.SearchSuggestionData
import com.handsome.lib.search.network.myCoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchSuggestionFragmentViewModel : ViewModel() {

    private val _mutableSearchSuggestionStateFlow = MutableSharedFlow<SearchSuggestionData?>()
    val searchSuggestionStateFlow : SharedFlow<SearchSuggestionData?>
        get() = _mutableSearchSuggestionStateFlow.asSharedFlow()


    fun getSearchSuggestion(keywords : String){
        viewModelScope.launch(myCoroutineExceptionHandler + Dispatchers.IO) {
            _mutableSearchSuggestionStateFlow.emit(SearchSuggestionApiService.INSTANCE.getSuggestionData(keywords))
        }
    }

}