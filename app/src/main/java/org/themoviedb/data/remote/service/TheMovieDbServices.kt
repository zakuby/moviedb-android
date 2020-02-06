package org.themoviedb.data.remote.service

import io.reactivex.Single
import org.themoviedb.data.remote.response.MovieCreditsResponse
import org.themoviedb.data.remote.response.MovieListResponse
import org.themoviedb.data.remote.response.TvShowListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbServices {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: String = "1"
    ): Single<MovieListResponse>

    @GET("tv/top_rated")
    fun getTopRatedTvShows(
        @Query("language") lang: String = "en-US",
        @Query("page") page: String = "1"
    ): Single<TvShowListResponse>

    @GET("movie/{id}/credits")
    fun getMovieCredits(
        @Path("id") id: String
    ): Single<MovieCreditsResponse>

    @GET("tv/{id}/credits")
    fun getTvShowCredits(
        @Path("id") id: String
    ): Single<MovieCreditsResponse>
}