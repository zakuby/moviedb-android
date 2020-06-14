package org.themoviedb.utils

import java.util.*

const val PROVIDER_AUTHORITY = "org.themoviedb.data.local.provider"

const val MOVIE_DATABASE = "TheMovieDb.db"

const val MOVIE_DATA = "movie"
const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original/"

const val NOTIFICATION_CHANNEL_ID = "notify-moviedb"
const val NOTIFICATION_ID = 322

const val WORKER_RELEASE_TAG = "worker-release"
const val WORKER_DAILY_TAG = "worker-daily"

fun getDelayNextDay(hours: Int): Long {
    val currentTime = System.currentTimeMillis()
    val nextDay = Calendar.getInstance().apply {
        val isNotSameDay = get(Calendar.HOUR_OF_DAY) > hours
        if (isNotSameDay) add(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, hours)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return nextDay.timeInMillis - currentTime
}