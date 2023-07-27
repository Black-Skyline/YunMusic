package com.handsome.lib.mv.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.handsome.lib.mv.network.api.MvApiService
import com.handsome.lib.mv.network.model.MvData
import com.handsome.lib.mv.network.model.MvRecommendData
import com.handsome.lib.mv.view.adapter.RecommendMvPagingSource
import com.handsome.lib.util.extention.toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

class MvViewModel : ViewModel() {

    private var _mutableMvLiveData = MutableLiveData<MvData>()
    val mvLiveData : LiveData<MvData>
        get() = _mutableMvLiveData



    fun getMvData(id : Long) {
        MvApiService.INSTANCE.getMvData(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<MvData>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    e.toString().toast()
                }

                override fun onSuccess(t: MvData) {
                    _mutableMvLiveData.value = t
                }
            })
    }

    fun getRecommendMvData() : Flow<PagingData<MvRecommendData.Data>> {
        return Pager(
            config = PagingConfig(5),
            pagingSourceFactory = {RecommendMvPagingSource()}
        ).flow
    }

}