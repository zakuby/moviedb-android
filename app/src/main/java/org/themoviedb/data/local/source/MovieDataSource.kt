package org.themoviedb.data.local.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.source.MovieDataSource.SourceType.*
import org.themoviedb.data.remote.response.ErrorResponse
import org.themoviedb.data.remote.response.ErrorResponseHandler
import org.themoviedb.data.remote.service.TheMovieDbServices
import org.themoviedb.utils.ext.disposedBy

class MovieDataSource constructor(
    private val service: TheMovieDbServices,
    private val errorResponseHandler: ErrorResponseHandler,
    private val sourceType: SourceType = DEFAULT,
    private val keywords: String = ""
) : PageKeyedDataSource<Int, Movie>() {

    enum class SourceType {
        BY_SEARCH_QUERY,
        BY_GENRES,
        DEFAULT
    }

    private val initialLoading = MutableLiveData<Boolean>()

    private val errorResponse = MutableLiveData<ErrorResponse>()

    private val initialEmpty = MutableLiveData<Boolean>().apply { postValue(false) }

    private val compositeDisposable = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        val movieServices = when (sourceType) {
            BY_SEARCH_QUERY -> service.searchMovies(keyword = keywords)
            BY_GENRES -> service.searchByGenres(genres = keywords)
            DEFAULT -> service.getPopularMovies()
        }
        movieServices.subscribeOn(Schedulers.io())
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        val movieServices = when (sourceType) {
            BY_SEARCH_QUERY -> service.searchMovies(page = params.key, keyword = keywords)
            BY_GENRES -> service.searchByGenres(page = params.key, genres = keywords)
            DEFAULT -> service.getPopularMovies(page = params.key)
        }
        movieServices.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { resp ->
                    resp.results ?: return@subscribeBy
                    callback.onResult(resp.results, params.key + 1)
                }, onError = { error -> Crashlytics.logException(error) }
            ).disposedBy(compositeDisposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {}

    fun getInitialLoading(): LiveData<Boolean> = initialLoading
    fun getInitialEmpty(): LiveData<Boolean> = initialEmpty
    fun getErrorResponse(): LiveData<ErrorResponse> = errorResponse
}