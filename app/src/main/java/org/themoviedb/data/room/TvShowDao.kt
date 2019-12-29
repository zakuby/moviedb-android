package org.themoviedb.data.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import org.themoviedb.data.models.TvShow

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tvshow: TvShow): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tvshows: List<TvShow>): List<Long>

    @Query("SELECT * FROM tvshow WHERE id = :id")
    fun selectById(id: String): Single<TvShow>

    @Query("SELECT * FROM tvshow")
    fun selectAll(): Single<List<TvShow>>

    @Update
    fun update(tvshow: TvShow): Int

    @Query("DELETE FROM tvshow WHERE id= :id")
    fun deleteByTvShow(id: String): Completable

    @Query("DELETE FROM tvshow")
    fun deleteAll(): Int
}