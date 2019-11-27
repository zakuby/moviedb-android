package org.themoviedb.data.repository

import io.reactivex.Single
import org.themoviedb.data.models.Movie
import org.themoviedb.data.room.Database
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val database: Database
) {

    fun removieMovie(id: Long) = database.movieDao().deleteById(id)

    fun saveMovie(movie: Movie) = database.movieDao().insert(movie)

    fun getMovies(): Single<List<Movie>> = database.movieDao().selectAll()
}