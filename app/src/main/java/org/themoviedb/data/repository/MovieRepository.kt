package org.themoviedb.data.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.themoviedb.data.models.Movie
import org.themoviedb.data.room.Database
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val database: Database
) {

    fun removeMovie(id: String) =
        database.movieDao()
            .deleteById(id)
            .subscribeOn(Schedulers.io())

    fun saveMovie(movie: Movie) =
        database.movieDao()
            .insert(movie)
            .subscribeOn(Schedulers.io())

    fun getMovies(): Single<List<Movie>> = database.movieDao().selectAll()

    fun getMovieById(id: String): Single<Movie> = database.movieDao().selectById(id)
}