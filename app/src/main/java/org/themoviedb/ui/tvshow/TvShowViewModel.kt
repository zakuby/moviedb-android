package org.themoviedb.ui.tvshow

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.data.local.source.TvShowDataSource
import org.themoviedb.data.local.source.TvShowDataSourceFactory
import org.themoviedb.data.remote.response.ErrorResponse
import org.themoviedb.ui.base.BaseViewModel
import javax.inject.Inject

class TvShowViewModel @Inject constructor(
    private val dataSourceFactory: TvShowDataSourceFactory
) : BaseViewModel() {

    val initialLoading: LiveData<Boolean>
    val initialEmpty: LiveData<Boolean>
    val errorResponse: LiveData<ErrorResponse>
    val tvShows: LiveData<PagedList<TvShow>>
    val searchQuery = ObservableField(dataSourceFactory.getKeywords())

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()
        tvShows = LivePagedListBuilder(dataSourceFactory, config).build()
        errorResponse = Transformations.switchMap(dataSourceFactory.getDataSource(), TvShowDataSource::getErrorResponse)
        initialLoading = Transformations.switchMap(dataSourceFactory.getDataSource(), TvShowDataSource::getInitialLoading)
        initialEmpty = Transformations.switchMap(dataSourceFactory.getDataSource(), TvShowDataSource::getInitialEmpty)
    }

    fun retryLoadTvShows() = dataSourceFactory.reloadInitial()

    fun searchMovies(keyword: String?) {
        searchQuery.set(keyword)
        dataSourceFactory.searchMovies(keyword)
    }

    override fun onCleared() {
        super.onCleared()
        dataSourceFactory.onClear()
    }
}