package com.handsome.module.find.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.module.find.network.api.FindAlbumApiService
import com.handsome.module.find.network.model.AlbumData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SpecialEditionViewModel : ViewModel() {

    private val _mutableAlbumStateFlow = MutableStateFlow<AlbumData?>(null)
    val stateFlow : StateFlow<AlbumData?>
        get() = _mutableAlbumStateFlow.asStateFlow()

    fun getAlbumData(id : Int){
        viewModelScope.launch {
            _mutableAlbumStateFlow.emit(FindAlbumApiService.INSTANCE.getAlbumData(id))
        }
    }

}