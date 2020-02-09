package org.themoviedb.data.local.room.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.data.local.room.FavoriteDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowRepository @Inject constructor(
    private val database: FavoriteDatabase
) {

    fun removeTvShow(id: Int) =
        database.tvShowDao()
            .deleteByTvShow(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun saveTvShow(tvShow: TvShow) =
        database.tvShowDao()
            .insert(tvShow)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getTvShows(): Single<List<TvShow>> =
        database.tvShowDao()
            .selectAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getTvShowById(id: Int): Single<TvShow> =
        database.tvShowDao()
            .selectById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}