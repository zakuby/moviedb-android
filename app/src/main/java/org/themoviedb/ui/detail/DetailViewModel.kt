package org.themoviedb.ui.detail

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.data.local.models.*
import org.themoviedb.data.local.room.repository.MovieRepository
import org.themoviedb.data.local.room.repository.TvShowRepository
import org.themoviedb.data.local.source.DetailReviewDataSource
import org.themoviedb.data.local.source.DetailReviewDataSourceFactory
import org.themoviedb.data.remote.service.TheMovieDbServices
import org.themoviedb.ui.base.BaseViewModel
import org.themoviedb.ui.detail.DetailViewModel.FavoriteAction.ActionFavoriteAdded
import org.themoviedb.ui.detail.DetailViewModel.FavoriteAction.ActionFavoriteRemoved
import org.themoviedb.utils.SingleLiveEvent
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val service: TheMovieDbServices,
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository
) : BaseViewModel() {

    private val casts = MutableLiveData<List<Cast>>()
    fun getMovieCasts(): LiveData<List<Cast>> = casts
    private val genres = MutableLiveData<List<Genre>>()
    fun getDetailGenre(): LiveData<List<Genre>> = genres

    val onReviewLiveDataReady = SingleLiveEvent<Unit>()
    private lateinit var reviews: LiveData<PagedList<Review>>
    fun getDetailReview(): LiveData<PagedList<Review>> = reviews

    private val _isMovie = ObservableBoolean(true)
    private val isMovie get() = _isMovie.get()

    val movie = MutableLiveData<Movie>()
    private val movieData get() = movie.value

    val isMovieFavorite = ObservableBoolean(false)
    private val isFavorite get() = isMovieFavorite.get()

    val movieFavoriteLoading = ObservableBoolean(true)
    val detailCastLoading = ObservableBoolean(false)
    lateinit var detailReviewLoading: LiveData<Boolean>

    sealed class FavoriteAction(val type: String) {
        class ActionFavoriteAdded(type: String) : FavoriteAction(type)
        class ActionFavoriteRemoved(type: String) : FavoriteAction(type)
    }

    private val favoriteAction = MutableLiveData<FavoriteAction>()
    fun getFavoriteButtonAction(): LiveData<FavoriteAction> = favoriteAction

    val isErrorCast = ObservableBoolean(false)

    lateinit var isErrorReview: LiveData<Boolean>

    fun setMovieDetail(id: Int, isMovie: Boolean) {
        this._isMovie.set(isMovie)
        fetchDetail(id)
        checkIsMovieFavorite(id)
    }

    private fun fetchDetail(id: Int) {
        val fetchDetailApi =
            if (isMovie) service.getMovieDetail(id) else service.getTvShowDetail(id).map { it.convertToMovie() }
        fetchDetailApi.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading() }
            .doAfterTerminate {
                finishLoading()
                fetchMovieCasts(id)
                fetchReviews(id)
            }
            .subscribeBy(onSuccess = { detail ->
                genres.postValue(detail.genres)
                movie.postValue(detail)
            })
            .disposedBy(compositeDisposable)
    }

    private fun checkIsMovieFavorite(id: Int) {
        if (isMovie) getMovieFromRepo(id)
        else getTvShowFromRepo(id)
    }

    private fun fetchMovieCasts(id: Int) {
        val fetchCredits =
            if (isMovie) service.getMovieCredits(id) else service.getTvShowCredits(id)

        fetchCredits.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { detailCastLoading.set(true) }
            .doAfterTerminate { detailCastLoading.set(false) }
            .subscribeBy(
                onSuccess = { resp ->
                    if (resp.cast.isNullOrEmpty()) isErrorCast.set(true)
                    else {
                        isErrorCast.set(false)
                        casts.postValue(resp.cast.take(10))
                    }
                }, onError = { error ->
                    isErrorCast.set(true)
                    Crashlytics.logException(error)
                }
            ).disposedBy(compositeDisposable)
    }

    private fun fetchReviews(id: Int) {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()
        val dataSource = DetailReviewDataSource(service, id, isMovie, compositeDisposable)
        val dataSourceFactory = DetailReviewDataSourceFactory(dataSource)
        reviews = LivePagedListBuilder(dataSourceFactory, config).build()
        detailReviewLoading = Transformations.switchMap(dataSourceFactory.getDataSource(), DetailReviewDataSource::getInitialLoading)
        isErrorReview = Transformations.switchMap(dataSourceFactory.getDataSource(), DetailReviewDataSource::getInitialEmpty)
        onReviewLiveDataReady.call()
    }

    private fun getMovieFromRepo(id: Int) {
        movieRepository.getMovieById(id)
            .doOnSubscribe { movieFavoriteLoading.set(true) }
            .doAfterTerminate { movieFavoriteLoading.set(false) }
            .subscribeBy(
                onSuccess = { movie -> isMovieFavorite.set(movie.isFavorite) },
                onError = { isMovieFavorite.set(false) }
            ).disposedBy(compositeDisposable)
    }

    private fun getTvShowFromRepo(id: Int) {
        tvShowRepository.getTvShowById(id)
            .doOnSubscribe { movieFavoriteLoading.set(true) }
            .doAfterTerminate { movieFavoriteLoading.set(false) }
            .subscribeBy(
                onSuccess = { tvShow -> isMovieFavorite.set(tvShow.isFavorite) },
                onError = { isMovieFavorite.set(false) }
            ).disposedBy(compositeDisposable)
    }

    fun updateRepository() {
        movieData?.let { movie ->
            if (isMovie) updateMovieRepository(movie)
            else updateFavoriteRepository(movie.convertToTvShow())
        }
    }

    private fun updateMovieRepository(movie: Movie) {
        if (isFavorite)
            movieRepository.removeMovie(movie.id)
                .subscribeBy(
                    onComplete = {
                        favoriteAction.postValue(ActionFavoriteRemoved("movie"))
                        isMovieFavorite.set(false)
                    },
                    onError = { error -> Crashlytics.logException(error) }
                ).disposedBy(compositeDisposable)
        else
            movieRepository.saveMovie(movie.apply { isFavorite = true })
                .subscribeBy(
                    onComplete = {
                        favoriteAction.postValue(ActionFavoriteAdded("movie"))
                        isMovieFavorite.set(true)
                    },
                    onError = { error -> Crashlytics.logException(error) }
                ).disposedBy(compositeDisposable)
    }

    private fun updateFavoriteRepository(tvShow: TvShow) {
        if (isFavorite)
            tvShowRepository.removeTvShow(tvShow.id)
                .subscribeBy(
                    onComplete = {
                        favoriteAction.postValue(ActionFavoriteRemoved("tv_show"))
                        isMovieFavorite.set(false)
                    },
                    onError = { error -> Crashlytics.logException(error) }
                ).disposedBy(compositeDisposable)
        else
            tvShowRepository.saveTvShow(tvShow.apply { isFavorite = true })
                .subscribeBy(
                    onComplete = {
                        favoriteAction.postValue(ActionFavoriteAdded("tv_show"))
                        isMovieFavorite.set(true)
                    },
                    onError = { error -> Crashlytics.logException(error) }
                ).disposedBy(compositeDisposable)
    }
}