package org.themoviedb.workers

import android.content.Context
import androidx.work.*
import com.google.gson.Gson
import io.reactivex.Single
import org.themoviedb.data.remote.service.TheMovieDbServices
import org.themoviedb.utils.IWorkerFactory
import org.themoviedb.utils.MOVIE_DATA
import org.themoviedb.utils.getDelayNextDay
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class ReleaseReminderWorker @Inject constructor (
    ctx: Context,
    params: WorkerParameters,
    private val services: TheMovieDbServices,
    private val gson: Gson
) : RxWorker(ctx, params)  {

    override fun createWork(): Single<Result> =
        services.getTodayReleaseMovies()
            .doOnSuccess { resp ->
                resp.results?.let { movies ->
                    movies.forEach { movie ->
                        val data = workDataOf(MOVIE_DATA to gson.toJson(movie))
                        val work = OneTimeWorkRequestBuilder<NotificationReleaseWorker>()
                            .setInputData(data)
                            .setInitialDelay(getDelayNextDay(8), TimeUnit.MILLISECONDS)
                            .build()
                        WorkManager.getInstance(applicationContext).enqueue(work)
                    }
                }
            }
            .map { Result.success() }
            .onErrorReturn { Result.failure() }


    class Factory @Inject constructor(
        private val context: Provider<Context>,
        private val services: Provider<TheMovieDbServices>,
        private val gson: Provider<Gson>
    ) : IWorkerFactory<ReleaseReminderWorker>{
        override fun create(params: WorkerParameters): ReleaseReminderWorker = ReleaseReminderWorker(context.get(), params, services.get(), gson.get())
    }
}