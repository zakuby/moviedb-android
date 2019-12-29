package org.themoviedb.screens.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.rxkotlin.subscribeBy
import org.themoviedb.core.base.BaseViewModel
import org.themoviedb.data.models.Movie
import org.themoviedb.data.repository.MovieRepository
import org.themoviedb.utils.SingleLiveEvent
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class FavoriteMovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseViewModel() {

    private val movies = MutableLiveData<List<Movie>>()

    val isMovieEmpty = MutableLiveData<Boolean>()

    val successRemoveEvent = SingleLiveEvent<Unit>()

    fun getFavoriteMovies(): LiveData<List<Movie>> = movies

    fun loadFavoritesMovieRepo(needToReload: Boolean = true) {
        movieRepository.getMovies()
            .doOnSubscribe { setLoading() }
            .doAfterTerminate { finishLoading() }
            .subscribeBy(
                onSuccess = { movies ->
                    if (!movies.isNullOrEmpty()){
                        if (needToReload) this.movies.postValue(movies)
                        isMovieEmpty.postValue(false)
                    } else isMovieEmpty.postValue(true)
                }, onError = { error -> Crashlytics.logException(error) }
            ).disposedBy(compositeDisposable)
    }

    fun removeMovieFromRepo(id: String) {
        movieRepository.removeMovie(id)
            .subscribeBy(
                onComplete = {
                    successRemoveEvent.call()
                    loadFavoritesMovieRepo(false)
                }, onError = { error -> Crashlytics.logException(error) }
            ).disposedBy(compositeDisposable)
    }

}