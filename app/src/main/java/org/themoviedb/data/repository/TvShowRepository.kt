package org.themoviedb.data.repository

import io.reactivex.Single
import org.themoviedb.data.models.TvShow
import org.themoviedb.data.room.Database
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowRepository @Inject constructor(
    private val database: Database
) {

    fun removieTvShow(id: Long) = database.tvShowDao().deleteById(id)

    fun saveTvShow(tvShow: TvShow) = database.tvShowDao().insert(tvShow)

    fun getTvShows(): Single<List<TvShow>> = database.tvShowDao().selectAll()
}