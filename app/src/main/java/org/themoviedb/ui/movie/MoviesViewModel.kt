package org.themoviedb.ui.movie

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
import org.themoviedb.data.local.models.Movie
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val service: TheMovieDbServices,
    private val errorResponseHandler: ErrorResponseHandler
) : BaseViewModel() {

    init {
        getPopularMovies()
    }

    private val movies = MutableLiveData<List<Movie>>()

    fun getMovies(): LiveData<List<Movie>> = movies

    val isError = ObservableBoolean(false)

    private val errorResponse = MutableLiveData<ErrorResponse>()

    fun getErrorResponse(): LiveData<ErrorResponse> = errorResponse

    fun getPopularMovies() {
        service.getPopularMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading() }
            .doAfterTerminate { finishLoading() }
            .subscribeBy(
                onSuccess = { resp ->
                    resp.results?.let { popularMovies ->
                        isError.set(false)
                        movies.postValue(popularMovies)
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