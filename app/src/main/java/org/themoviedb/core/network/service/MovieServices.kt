package org.themoviedb.core.network.service

import io.reactivex.Single
import org.themoviedb.core.network.response.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieServices {

    @GET("movie/popular")
    fun getPopularMovies(@Query("language") lang: String = "en-US",
                         @Query("page") page: String = "1") : Single<MovieListResponse>

}