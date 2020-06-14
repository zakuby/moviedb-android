package org.themoviedb.data.local.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.data.remote.response.ErrorResponse
import org.themoviedb.data.remote.response.ErrorResponseHandler
import org.themoviedb.data.remote.service.TheMovieDbServices
import org.themoviedb.utils.ext.disposedBy

class TvShowDataSource constructor(
    private val service: TheMovieDbServices,
    private val errorResponseHandler: ErrorResponseHandler,
    private val keyword: String? = ""
) : PageKeyedDataSource<Int, TvShow>() {

    private val initialLoading = MutableLiveData<Boolean>()

    private val errorResponse = MutableLiveData<ErrorResponse>()

    private val initialEmpty = MutableLiveData<Boolean>().apply { postValue(false) }

    private val compositeDisposable = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, TvShow>) {
        val tvShowServices = if (keyword.isNullOrEmpty()) service.getTopRatedTvShows() else service.searchTvShows(keyword = keyword)
        tvShowServices.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { initialLoading.postValue(true) }
            .doAfterTerminate { initialLoading.postValue(false) }
            .subscribeBy(
                onSuccess = { resp ->
                    resp.results?.let { popularMovies ->
                        initialEmpty.postValue(false)
                        callback.onResult(popularMovies, null, 2)
                    } ?: initialEmpty.postValue(true)
                }, onError = { error ->
                    val errResp = errorResponseHandler.handleException(error)
                    errorResponse.postValue(errResp)
                    initialEmpty.postValue(true)
                    Crashlytics.logException(error)
                }
            ).disposedBy(compositeDisposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TvShow>) {
        val tvShowServices = if (keyword.isNullOrEmpty()) service.getTopRatedTvShows(page = params.key) else service.searchTvShows(page = params.key, keyword = keyword)
        tvShowServices.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { resp ->
                    resp.results ?: return@subscribeBy
                    callback.onResult(resp.results, params.key + 1)
                }, onError = { error -> Crashlytics.logException(error) }
            ).disposedBy(compositeDisposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TvShow>) {}

    fun getInitialLoading(): LiveData<Boolean> = initialLoading
    fun getInitialEmpty(): LiveData<Boolean> = initialEmpty
    fun getErrorResponse(): LiveData<ErrorResponse> = errorResponse
    fun onClear() {
        if (!compositeDisposable.isDisposed) compositeDisposable.clear()
    }
}