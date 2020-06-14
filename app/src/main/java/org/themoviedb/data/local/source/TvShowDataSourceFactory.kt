package org.themoviedb.data.local.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.data.remote.response.ErrorResponseHandler
import org.themoviedb.data.remote.service.TheMovieDbServices
import javax.inject.Inject

class TvShowDataSourceFactory @Inject constructor(
    private val service: TheMovieDbServices,
    private val errorResponseHandler: ErrorResponseHandler
) : DataSource.Factory<Int, TvShow>() {

    private lateinit var dataSource: TvShowDataSource

    private val dataSourceLiveData = MutableLiveData<TvShowDataSource>()

    private var keyword: String? = null

    override fun create(): DataSource<Int, TvShow> {
        dataSource = TvShowDataSource(service, errorResponseHandler, keyword)
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }

    fun reloadInitial() = dataSource.invalidate()

    fun searchMovies(keyword: String?) {
        this.keyword = keyword
        dataSource.invalidate()
    }

    fun getKeywords(): String = keyword ?: ""

    fun getDataSource(): LiveData<TvShowDataSource> = dataSourceLiveData

    fun onClear() = dataSource.onClear()
}