package org.themoviedb.screens.tvshow.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.core.base.BaseViewModel
import org.themoviedb.core.network.response.ErrorResponse
import org.themoviedb.core.network.response.ErrorResponseHandler
import org.themoviedb.core.network.service.TheMovieDbServices
import org.themoviedb.models.TvShow
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class TvShowViewModel @Inject constructor(
    private val service: TheMovieDbServices,
    private val errorResponseHandler: ErrorResponseHandler
) : BaseViewModel() {
    init {
        getPopularMovies()
    }

    private val tvShows = MutableLiveData<List<TvShow>>()

    fun getTvShows(): LiveData<List<TvShow>> = tvShows

    val isResponseError = ObservableBoolean(false)

    private val errorResponse = MutableLiveData<ErrorResponse>()

    fun getErrorResponse(): LiveData<ErrorResponse> = errorResponse

    private fun getPopularMovies() {
        service.getTopRatedTvShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading() }
            .doAfterTerminate { finishLoading() }
            .subscribeBy(
                onSuccess = { resp ->
                    resp.results?.let { popularMovies ->
                        tvShows.postValue(popularMovies)
                    }
                }, onError = { error ->
                    val errResp = errorResponseHandler.handleException(error)
                    errorResponse.postValue(errResp)
                    Crashlytics.logException(error)
                }
            ).disposedBy(compositeDisposable)
    }
}