package org.themoviedb.data.remote.service

import io.reactivex.Single
import org.themoviedb.data.remote.response.MovieCreditsResponse
import org.themoviedb.data.remote.response.MovieListResponse
import org.themoviedb.data.remote.response.TvShowListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

interface TheMovieDbServices {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1
    ): Single<MovieListResponse>

    @GET("discover/movie")
    fun getTodayReleaseMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("primary_release_date.gte") dateGte: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
        @Query("primary_release_date.lte") dateLte: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    ): Single<MovieListResponse>

    @GET("search/movie")
    fun searchMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("query") keyword: String
    ): Single<MovieListResponse>

    @GET("tv/top_rated")
    fun getTopRatedTvShows(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1
    ): Single<TvShowListResponse>

    @GET("search/tv")
    fun searchTvShows(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("query") keyword: String
    ): Single<TvShowListResponse>

    @GET("movie/{id}/credits")
    fun getMovieCredits(
        @Path("id") id: Int
    ): Single<MovieCreditsResponse>

    @GET("tv/{id}/credits")
    fun getTvShowCredits(
        @Path("id") id: Int
    ): Single<MovieCreditsResponse>
}