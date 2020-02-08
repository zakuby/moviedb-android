package org.themoviedb.utils

import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface IWorkerFactory<T: ListenableWorker> {
    fun create(params: WorkerParameters): T
}