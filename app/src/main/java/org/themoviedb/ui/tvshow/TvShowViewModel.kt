package org.themoviedb.ui.tvshow

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.ui.base.BaseViewModel
import org.themoviedb.data.remote.response.ErrorResponse
import org.themoviedb.data.remote.response.ErrorResponseHandler
import org.themoviedb.data.remote.service.TheMovieDbServices
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class TvShowViewModel @Inject constructor(
    private val service: TheMovieDbServices,
    private val errorResponseHandler: ErrorResponseHandler
) : BaseViewModel() {
    init {
        getTopRatedTvShows()
    }

    private val tvShows = MutableLiveData<List<TvShow>>()

    fun getTvShows(): LiveData<List<TvShow>> = tvShows

    val isError = ObservableBoolean(false)

    private val errorResponse = MutableLiveData<ErrorResponse>()

    fun getErrorResponse(): LiveData<ErrorResponse> = errorResponse

    fun getTopRatedTvShows() {
        service.getTopRatedTvShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading() }
            .doAfterTerminate { finishLoading() }
            .subscribeBy(
                onSuccess = { resp ->
                    resp.results?.let { popularMovies ->
                        isError.set(false)
                        tvShows.postValue(popularMovies)
                    } ?: isError.set(true)
                }, onError = { error ->
                    val errResp = errorResponseHandler.handleException(error)
                    errorResponse.postValue(errResp)
                    Crashlytics.logException(error)
                    isError.set(true)
                }
            ).disposedBy(compositeDisposable)
    }
}