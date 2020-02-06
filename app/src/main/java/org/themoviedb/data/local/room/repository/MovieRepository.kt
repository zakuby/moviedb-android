package org.themoviedb.data.local.room.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.room.Database
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
            .observeOn(AndroidSchedulers.mainThread())

    fun saveMovie(movie: Movie) =
        database.movieDao()
            .insert(movie)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getMovies(): Single<List<Movie>> =
        database.movieDao()
            .selectAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getMovieById(id: String): Single<Movie> =
        database.movieDao()
            .selectById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}