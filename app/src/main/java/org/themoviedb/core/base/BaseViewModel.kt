package org.themoviedb.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    private val loading = MutableLiveData<Boolean>()

    fun getLoading(): LiveData<Boolean> = loading

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) compositeDisposable.clear()
    }

    protected fun setLoading() = loading.postValue(true)

    protected fun finishLoading() = loading.postValue(false)
}