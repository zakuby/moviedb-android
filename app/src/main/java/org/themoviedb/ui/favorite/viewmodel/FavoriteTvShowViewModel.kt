package org.themoviedb.ui.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.rxkotlin.subscribeBy
import org.themoviedb.ui.base.BaseViewModel
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.data.local.room.repository.TvShowRepository
import org.themoviedb.utils.SingleLiveEvent
import org.themoviedb.utils.ext.disposedBy
import javax.inject.Inject

class FavoriteTvShowViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository
) : BaseViewModel() {

    private val tvShows = MutableLiveData<List<TvShow>>()

    val isTvShowEmpty = MutableLiveData<Boolean>()

    val successRemoveEvent = SingleLiveEvent<Unit>()

    fun getTvShows(): LiveData<List<TvShow>> = tvShows

    fun loadTvShowRepo(needToReload: Boolean = true) {
        tvShowRepository.getTvShows()
            .subscribeBy(
                onSuccess = { tvShows ->
                    if (!tvShows.isNullOrEmpty()){
                        if (needToReload) this.tvShows.postValue(tvShows)
                        isTvShowEmpty.postValue(false)
                    } else isTvShowEmpty.postValue(true)
                }, onError = { error -> Crashlytics.logException(error) }
            ).disposedBy(compositeDisposable)
    }

    fun removeTvShowFromRepo(id: Int) {
        tvShowRepository.removeTvShow(id)
            .subscribeBy(
                onComplete = {
                    successRemoveEvent.call()
                    loadTvShowRepo(false)
                }, onError = { error -> Crashlytics.logException(error) }
            ).disposedBy(compositeDisposable)
    }
}