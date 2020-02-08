package org.themoviedb.data.local.room.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import org.themoviedb.data.local.models.TvShow

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tvshow: TvShow): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tvshows: List<TvShow>): List<Long>

    @Query("SELECT * FROM tvshow WHERE id = :id")
    fun selectById(id: Int): Single<TvShow>

    @Query("SELECT * FROM tvshow")
    fun selectAll(): Single<List<TvShow>>

    @Update
    fun update(tvshow: TvShow): Int

    @Query("DELETE FROM tvshow WHERE id= :id")
    fun deleteByTvShow(id: Int): Completable

    @Query("DELETE FROM tvshow")
    fun deleteAll(): Int
}