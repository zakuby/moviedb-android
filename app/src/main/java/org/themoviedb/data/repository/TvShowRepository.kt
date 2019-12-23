package org.themoviedb.data.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.themoviedb.data.models.TvShow
import org.themoviedb.data.room.Database
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowRepository @Inject constructor(
    private val database: Database
) {

    fun removeTvShow(id: String) =
        database.tvShowDao()
            .deleteByTvShow(id)
            .subscribeOn(Schedulers.io())

    fun saveTvShow(tvShow: TvShow) =
        database.tvShowDao()
            .insert(tvShow)
            .subscribeOn(Schedulers.io())

    fun getTvShows(): Single<List<TvShow>> = database.tvShowDao().selectAll()

    fun getTvShowById(id: String): Single<TvShow> = database.tvShowDao().selectById(id)
}