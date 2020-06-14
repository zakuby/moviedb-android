package org.themoviedb.data.local.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.data.local.models.Review
import org.themoviedb.data.remote.service.TheMovieDbServices
import org.themoviedb.utils.ext.disposedBy

class DetailReviewDataSource constructor(
    private val service: TheMovieDbServices,
    private val detailId: Int,
    private val isMovie: Boolean = true,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Review>() {

    private val initialLoading = MutableLiveData<Boolean>().apply { postValue(false) }

    private val initialEmpty = MutableLiveData<Boolean>().apply { postValue(false) }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Review>
    ) {
        val fetchReviews =
            if (isMovie) service.getMovieReviews(id = detailId) else service.getTvShowReviews(id = detailId)

        fetchReviews.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                initialLoading.postValue(true) }
            .doAfterTerminate { initialLoading.postValue(false) }
            .subscribeBy(
                onSuccess = { resp ->
                    if (resp.results.isNullOrEmpty()) initialEmpty.postValue(true)
                    else {
                        initialEmpty.postValue(false)
                        callback.onResult(resp.results, null, 2)
                    }
                }, onError = { error ->
                    initialEmpty.postValue(true)
                    Crashlytics.logException(error)
                }
            ).disposedBy(compositeDisposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Review>) {
        val fetchReviews =
            if (isMovie) service.getMovieReviews(page = params.key, id = detailId) else service.getTvShowReviews(page = params.key, id = detailId)
        fetchReviews.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { resp ->
                    resp.results ?: return@subscribeBy
                    callback.onResult(resp.results, params.key + 1)
                }, onError = { error -> Crashlytics.logException(error) }
            ).disposedBy(compositeDisposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Review>) {}

    fun getInitialLoading(): LiveData<Boolean> = initialLoading
    fun getInitialEmpty(): LiveData<Boolean> = initialEmpty
}