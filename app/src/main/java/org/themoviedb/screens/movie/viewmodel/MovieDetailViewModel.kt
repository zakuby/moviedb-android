package org.themoviedb.screens.movie.viewmodel

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
import org.themoviedb.core.network.service.MovieServices
import org.themoviedb.models.Cast
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val service: MovieServices,
    private val errorResponseHandler: ErrorResponseHandler
) : BaseViewModel() {

    private val casts = MutableLiveData<List<Cast>>()

    fun getMovieCasts(): LiveData<List<Cast>> = casts

    val isResponseError = ObservableBoolean(false)

    private val errorResponse = MutableLiveData<ErrorResponse>()

    fun getErrorResponse(): LiveData<ErrorResponse> = errorResponse

    fun fetchMovieCasts(id: String) {
        service.getMovieCredits(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading() }
            .doAfterTerminate { finishLoading() }
            .subscribeBy(
                onSuccess = { resp ->
                    resp.cast?.let { respCast ->
                        casts.postValue(respCast.take(10))
                    }
                }, onError = { error ->
                    val errResp = errorResponseHandler.handleException(error)
                    errorResponse.postValue(errResp)
                    Crashlytics.logException(error)
                }
            ).disposedBy(compositeDisposable)
    }
}