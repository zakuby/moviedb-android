package org.themoviedb.ui.movie

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.data.local.models.Genre
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.source.MovieDataSource
import org.themoviedb.data.local.source.MovieDataSourceFactory
import org.themoviedb.data.remote.response.ErrorResponse
import org.themoviedb.data.remote.service.TheMovieDbServices
import org.themoviedb.ui.base.BaseViewModel
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val dataSourceFactory: MovieDataSourceFactory,
    private val ApiServices: TheMovieDbServices
) : BaseViewModel() {

    val initialLoading: LiveData<Boolean>
    val initialEmpty: LiveData<Boolean>
    val errorResponse: LiveData<ErrorResponse>
    val movies: LiveData<PagedList<Movie>>
    val searchQuery = ObservableField(dataSourceFactory.getKeywords())

    private val _movieGenres: MutableLiveData<List<Genre>> = MutableLiveData()
    val movieGenres: LiveData<List<Genre>> = _movieGenres

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()
        movies = LivePagedListBuilder(dataSourceFactory, config).build()
        errorResponse = Transformations.switchMap(dataSourceFactory.getDataSource(), MovieDataSource::getErrorResponse)
        initialLoading = Transformations.switchMap(dataSourceFactory.getDataSource(), MovieDataSource::getInitialLoading)
        initialEmpty = Transformations.switchMap(dataSourceFactory.getDataSource(), MovieDataSource::getInitialEmpty)
        fetchMovieGenres()
    }

    fun retryLoadMovies() = dataSourceFactory.reloadInitial()

    private fun fetchMovieGenres() = ApiServices.getMovieGenres()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy { resp -> resp.genres?.let { _movieGenres.postValue(it) } }
        .disposedBy(compositeDisposable)

    fun searchMovies(keyword: String?) {
        searchQuery.set(keyword)
        dataSourceFactory.searchMovies(keyword)
    }

    fun searchByGenres(genres: String?) {
        dataSourceFactory.searchMovies(genres, true)
    }
}