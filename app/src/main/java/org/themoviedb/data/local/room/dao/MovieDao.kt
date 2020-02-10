package org.themoviedb.data.local.room.dao

import android.database.Cursor
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.models.Movie.Companion.TABLE_NAME

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCursor(movie: Movie): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>): List<Long>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    fun selectById(id: Int): Single<Movie>


    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    fun selectByIdCursor(id: Int): Cursor

    @Query("SELECT * FROM $TABLE_NAME")
    fun selectAll(): Single<List<Movie>>

    @Query("SELECT * FROM $TABLE_NAME")
    fun selectAllCursor(): Cursor

    @Query("SELECT * FROM $TABLE_NAME")
    fun selectAllForWidget(): List<Movie>

    @Update
    fun update(movie: Movie): Int

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    fun deleteById(id: Int): Completable

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    fun deleteByIdCursor(id: Int): Int


    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAll(): Int
}