package com.handsome.lib.mv.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.handsome.lib.mv.network.api.MvApiService
import com.handsome.lib.mv.network.api.MvRecommendApiService
import com.handsome.lib.mv.network.model.MvData
import com.handsome.lib.mv.network.model.MvRecommendData
import com.handsome.lib.util.extention.toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MvViewModel : ViewModel() {

    private var _mutableMvLiveData = MutableLiveData<MvData>()
    val mvLiveData : LiveData<MvData>
        get() = _mutableMvLiveData

    private var _mutableMvRecommendLiveData = MutableLiveData<MvRecommendData>()
    val mvRecommendLiveData  : LiveData<MvRecommendData>
        get() = _mutableMvRecommendLiveData



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

    fun getRecommendMvData(){
        MvRecommendApiService.INSTANCE.getRecommendData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<MvRecommendData>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    e.toString().toast()
                }

                override fun onSuccess(t: MvRecommendData) {
                    _mutableMvRecommendLiveData.value = t
                }
            })
    }

}