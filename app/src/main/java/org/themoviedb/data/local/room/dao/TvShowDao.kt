package org.themoviedb.data.local.room.dao

import android.database.Cursor
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import org.themoviedb.data.local.models.TvShow

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tvShow: TvShow): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCursor(tvShow: TvShow): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tvShows: List<TvShow>): List<Long>

    @Query("SELECT * FROM ${TvShow.TABLE_NAME} WHERE id = :id")
    fun selectByIdCursor(id: Int): Cursor

    @Query("SELECT * FROM ${TvShow.TABLE_NAME} WHERE id = :id")
    fun selectById(id: Int): Single<TvShow>

    @Query("SELECT * FROM ${TvShow.TABLE_NAME}")
    fun selectAll(): Single<List<TvShow>>

    @Query("SELECT * FROM ${TvShow.TABLE_NAME}")
    fun selectAllCursor(): Cursor

    @Update
    fun update(tvShow: TvShow): Int

    @Query("DELETE FROM ${TvShow.TABLE_NAME} WHERE id= :id")
    fun deleteByTvShow(id: Int): Completable

    @Query("DELETE FROM ${TvShow.TABLE_NAME} WHERE id = :id")
    fun deleteByIdCursor(id: Int): Int

    @Query("DELETE FROM ${TvShow.TABLE_NAME}")
    fun deleteAll(): Int
}