package org.themoviedb.screens.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.themoviedb.core.base.BaseViewModel
import org.themoviedb.data.models.TvShow
import org.themoviedb.data.repository.TvShowRepository
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class FavoriteTvShowViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository
) : BaseViewModel() {

    init {
        getTvShowRepo()
    }

    private val tvShows = MutableLiveData<List<TvShow>>()

    fun getTvShows(): LiveData<List<TvShow>> = tvShows

    private fun getTvShowRepo() {
        tvShowRepository.getTvShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading() }
            .doAfterTerminate { finishLoading() }
            .subscribeBy(
                onSuccess = { tvShowRepo ->
                    tvShowRepo?.let { favoriteTvShows -> tvShows.postValue(favoriteTvShows) }
                }, onError = { error -> Crashlytics.logException(error) }
            ).disposedBy(compositeDisposable)
    }
}