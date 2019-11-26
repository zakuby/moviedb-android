package org.themoviedb.screens.main.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.core.base.BaseViewModel
import org.themoviedb.core.network.response.ErrorResponse
import org.themoviedb.core.network.response.ErrorResponseHandler
import org.themoviedb.core.network.response.MovieCreditsResponse
import org.themoviedb.core.network.service.TheMovieDbServices
import org.themoviedb.models.Cast
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val service: TheMovieDbServices,
    private val errorResponseHandler: ErrorResponseHandler
) : BaseViewModel() {

    private val casts = MutableLiveData<List<Cast>>()

    fun getMovieCasts(): LiveData<List<Cast>> = casts

    val isResponseError = ObservableBoolean(false)

    private val errorResponse = MutableLiveData<ErrorResponse>()

    fun getErrorResponse(): LiveData<ErrorResponse> = errorResponse

    fun fetchMovieCasts(id: String, isMovieCast: Boolean) {
        val fetchCredits =
            if (isMovieCast) service.getMovieCredits(id) else service.getTvShowCredits(id)

        fetchCredits.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading() }
            .doAfterTerminate { finishLoading() }
            .subscribeBy(
                onSuccess = { resp ->
                    resp.cast?.let { respCast ->
                        isResponseError.set(false)
                        casts.postValue(respCast.take(10))
                    } ?: isResponseError.set(true)
                }, onError = { error ->
                    val errResp = errorResponseHandler.handleException(error)
                    isResponseError.set(true)
                    errorResponse.postValue(errResp)
                    Crashlytics.logException(error)
                }
            ).disposedBy(compositeDisposable)
    }
}