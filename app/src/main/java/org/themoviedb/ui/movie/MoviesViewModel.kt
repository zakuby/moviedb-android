package org.themoviedb.ui.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.source.MovieDataSource
import org.themoviedb.data.local.source.MovieDataSourceFactory
import org.themoviedb.data.remote.response.ErrorResponse
import org.themoviedb.ui.base.BaseViewModel
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val dataSourceFactory: MovieDataSourceFactory
): BaseViewModel() {

    val initialLoading: LiveData<Boolean>
    val initialEmpty: LiveData<Boolean>
    val errorResponse: LiveData<ErrorResponse>
    val movies: LiveData<PagedList<Movie>>

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
    }

    fun retryLoadMovies() = dataSourceFactory.reloadInitial()
}