package org.themoviedb.di.module

import androidx.work.ListenableWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.themoviedb.di.WorkerKey
import org.themoviedb.utils.IWorkerFactory
import org.themoviedb.workers.NotificationDailyWorker
import org.themoviedb.workers.NotificationReleaseWorker
import org.themoviedb.workers.ReleaseReminderWorker

@Module
interface WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(ReleaseReminderWorker::class)
    fun bindReleaseReminderWorker(factory: ReleaseReminderWorker.Factory): IWorkerFactory<out ListenableWorker>

    @Binds
    @IntoMap
    @WorkerKey(NotificationReleaseWorker::class)
    fun bindNotificationReleaseWorker(factory: NotificationReleaseWorker.Factory): IWorkerFactory<out ListenableWorker>

    @Binds
    @IntoMap
    @WorkerKey(NotificationDailyWorker::class)
    fun bindNotificationDailyWorker(factory: NotificationDailyWorker.Factory): IWorkerFactory<out ListenableWorker>
}