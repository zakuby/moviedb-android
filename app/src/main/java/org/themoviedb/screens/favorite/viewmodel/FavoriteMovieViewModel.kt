package org.themoviedb.screens.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.core.base.BaseViewModel
import org.themoviedb.data.models.Movie
import org.themoviedb.data.repository.MovieRepository
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class FavoriteMovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseViewModel() {

    init {
        getFavoritesMovieRepo()
    }

    private val movies = MutableLiveData<List<Movie>>()

    fun getFavoriteMovies(): LiveData<List<Movie>> = movies

    private fun getFavoritesMovieRepo() {
        movieRepository.getMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading() }
            .doAfterTerminate { finishLoading() }
            .subscribeBy(
                onSuccess = { moviesRepo ->
                    moviesRepo?.let { favoriteMovies ->
                        movies.postValue(favoriteMovies)
                    }
                }, onError = { error ->
                    Crashlytics.logException(error)
                }
            ).disposedBy(compositeDisposable)
    }

}