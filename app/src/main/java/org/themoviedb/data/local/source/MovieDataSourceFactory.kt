package org.themoviedb.data.local.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.remote.response.ErrorResponseHandler
import org.themoviedb.data.remote.service.TheMovieDbServices
import javax.inject.Inject

class MovieDataSourceFactory @Inject constructor(
    private val service: TheMovieDbServices,
    private val errorResponseHandler: ErrorResponseHandler
) : DataSource.Factory<Int, Movie>() {

    private lateinit var dataSource: MovieDataSource

    private val dataSourceLiveData = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        dataSource = MovieDataSource(service, errorResponseHandler)
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }

    fun reloadInitial() = dataSource.invalidate()


    fun getDataSource(): LiveData<MovieDataSource> = dataSourceLiveData
}