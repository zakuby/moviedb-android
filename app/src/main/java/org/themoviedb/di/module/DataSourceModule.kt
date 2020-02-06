package org.themoviedb.di.module

import dagger.Module
import dagger.Provides
import org.themoviedb.data.local.source.MovieDataSourceFactory
import org.themoviedb.data.local.source.TvShowDataSourceFactory
import org.themoviedb.data.remote.response.ErrorResponseHandler
import org.themoviedb.data.remote.service.TheMovieDbServices
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideMovieDataSourceFactory(
        services: TheMovieDbServices,
        errorResponseHandler: ErrorResponseHandler
    ) = MovieDataSourceFactory(services, errorResponseHandler)

    @Provides
    @Singleton
    fun provideTvShowDataSourceFactory(
        services: TheMovieDbServices,
        errorResponseHandler: ErrorResponseHandler
    ) = TvShowDataSourceFactory(services, errorResponseHandler)
}