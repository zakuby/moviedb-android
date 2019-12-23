package org.themoviedb.screens.main.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.core.base.BaseViewModel
import org.themoviedb.core.network.service.TheMovieDbServices
import org.themoviedb.data.models.Cast
import org.themoviedb.data.models.Movie
import org.themoviedb.data.models.TvShow
import org.themoviedb.data.repository.MovieRepository
import org.themoviedb.data.repository.TvShowRepository
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val service: TheMovieDbServices,
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository
) : BaseViewModel() {

    private val casts = MutableLiveData<List<Cast>>()

    val movie = MutableLiveData<Movie>()
    private val movieData get() = movie.value

    val isMovieFavorite = ObservableBoolean(false)
    private val isFavorite get() = isMovieFavorite.get()

    val movieFavoriteLoading = ObservableBoolean(true)

    fun getMovieCasts(): LiveData<List<Cast>> = casts

    val isError = ObservableBoolean(false)

    fun setMovieDetail(movie: Movie) {
        checkIsMovieFavorite(movie)
        this.movie.postValue(movie)
        this.fetchMovieCasts(movie.id, movie.isMovie ?: true)
    }

    private fun checkIsMovieFavorite(movie: Movie) {
        val isMovie = movie.isMovie ?: true
        val id = movie.id
        if (isMovie) getMovieFromRepo(id)
        else getTvShowFromRepo(id)

    }

    private fun getMovieFromRepo(id: String) {
        movieRepository.getMovieById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { movieFavoriteLoading.set(true) }
            .doAfterTerminate { movieFavoriteLoading.set(false) }
            .subscribeBy(
                onSuccess = { movie -> isMovieFavorite.set(movie.isFavorite) },
                onError = { isMovieFavorite.set(false) }
            ).disposedBy(compositeDisposable)
    }

    private fun getTvShowFromRepo(id: String) {
        tvShowRepository.getTvShowById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { movieFavoriteLoading.set(true) }
            .doAfterTerminate { movieFavoriteLoading.set(false) }
            .subscribeBy(
                onSuccess = { tvShow -> isMovieFavorite.set(tvShow.isFavorite) },
                onError = { isMovieFavorite.set(false) }
            ).disposedBy(compositeDisposable)
    }

    private fun fetchMovieCasts(id: String, isMovieCast: Boolean) {
        val fetchCredits =
            if (isMovieCast) service.getMovieCredits(id) else service.getTvShowCredits(id)

        fetchCredits.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading() }
            .doAfterTerminate { finishLoading() }
            .subscribeBy(
                onSuccess = { resp ->
                    resp.cast?.let { respCast ->
                        isError.set(false)
                        casts.postValue(respCast.take(10))
                    } ?: isError.set(true)
                }, onError = { error ->
                    isError.set(true)
                    Crashlytics.logException(error)
                }
            ).disposedBy(compositeDisposable)
    }

    fun updateRepository() {
        movieData?.let { movie ->
            val isMovie = movie.isMovie ?: true
            if (isMovie) updateMovieRepository(movie)
            else updateFavoriteRepository(movie.convertToTvShow())
        }
    }

    private fun updateMovieRepository(movie: Movie) {
        if (isFavorite)
            movieRepository.removeMovie(movie.id)
                .subscribeBy(
                    onComplete = { isMovieFavorite.set(false) },
                    onError = { error -> Crashlytics.logException(error) }
                ).disposedBy(compositeDisposable)
        else
            movieRepository.saveMovie(movie.apply { isFavorite = true })
                .subscribeBy(
                    onComplete = { isMovieFavorite.set(true) },
                    onError = { error -> Crashlytics.logException(error) }
                ).disposedBy(compositeDisposable)
    }

    private fun updateFavoriteRepository(tvShow: TvShow) {
        if (isFavorite)
            tvShowRepository.removeTvShow(tvShow.id)
                .subscribeBy(
                    onComplete = { isMovieFavorite.set(false) },
                    onError = { error -> Crashlytics.logException(error) }
                ).disposedBy(compositeDisposable)
        else
            tvShowRepository.saveTvShow(tvShow.apply { isFavorite = true })
                .subscribeBy(
                    onComplete = { isMovieFavorite.set(true) },
                    onError = { error -> Crashlytics.logException(error) }
                ).disposedBy(compositeDisposable)
    }
}