package com.handsome.module.find.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.module.find.network.api.FindTopListApiService
import com.handsome.module.find.network.exception.myCoroutineExceptionHandler
import com.handsome.module.find.network.model.TopListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopListViewModel : ViewModel() {

    private val _mutableTopListStateFlow = MutableStateFlow<TopListData?>(null)
    val topListStateFlow: StateFlow<TopListData?>
        get() = _mutableTopListStateFlow.asStateFlow()


    fun getTopList() {
        viewModelScope.launch(myCoroutineExceptionHandler + Dispatchers.IO) {
            _mutableTopListStateFlow.emit(FindTopListApiService.INSTANCE.getTopList())
        }
    }
}

/**
 * nt了，没看到有对应的接口
 * 这段相比于其它的网络请求有点复杂，好好解释一下
 * 首先开启了一个协程
 * 用于获取当前排行榜
 * 又因为我们可以看到排行榜右边有歌曲
 * 所以我们需要根据排行榜返回的数据来请求数据
 * 用sync相当于又开启协程，不阻塞，异步获取结果
 * 最后将结果一起呈现给收集方
 * val instance = FindTopListApiService.INSTANCE
val topList = instance.getTopList()
val waitResult = HashMap<Long, Deferred<MusicListDetail>>()
for (i in topList.list) {
val deferred = async {
instance.getTopListDetail(i.id)
}
waitResult[i.id] = deferred
}
//todo 记得网络拥挤的情况
for (i in 0 until topList.list.size) {
val item = topList.list[i]
item.musicListDetail = waitResult[item.id]?.await()
}
_mutableTopListStateFlow.emit(topList)
 */