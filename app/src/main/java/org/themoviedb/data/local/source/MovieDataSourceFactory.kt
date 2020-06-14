package org.themoviedb.data.local.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.source.MovieDataSource.SourceType
import org.themoviedb.data.remote.response.ErrorResponseHandler
import org.themoviedb.data.remote.service.TheMovieDbServices
import javax.inject.Inject

class MovieDataSourceFactory @Inject constructor(
    private val service: TheMovieDbServices,
    private val errorResponseHandler: ErrorResponseHandler
) : DataSource.Factory<Int, Movie>() {

    private lateinit var dataSource: MovieDataSource

    private val dataSourceLiveData = MutableLiveData<MovieDataSource>()

    private var sourceType: SourceType = SourceType.DEFAULT

    private var keywords: String? = ""

    override fun create(): DataSource<Int, Movie> {
        dataSource = MovieDataSource(service, errorResponseHandler, sourceType, keywords ?: "")
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }

    fun reloadInitial() = dataSource.invalidate()

    fun searchMovies(keyword: String?, isByGenre: Boolean = false) {
        this.keywords = keyword ?: ""
        this.sourceType =
            if (isByGenre) SourceType.BY_GENRES else if (keyword.isNullOrEmpty()) SourceType.DEFAULT else SourceType.BY_SEARCH_QUERY
        dataSource.invalidate()
    }

    fun getKeywords(): String = if (sourceType == SourceType.BY_SEARCH_QUERY) keywords ?: "" else ""

    fun getDataSource(): LiveData<MovieDataSource> = dataSourceLiveData
}