package org.themoviedb.data.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import org.themoviedb.data.models.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>): List<Long>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun selectById(id: String): Single<Movie>

    @Query("SELECT * FROM movie")
    fun selectAll(): Single<List<Movie>>

    @Update
    fun update(movie: Movie): Int

    @Query("DELETE FROM movie WHERE id = :id")
    fun deleteById(id: String): Completable

    @Query("DELETE FROM movie")
    fun deleteAll(): Int
}