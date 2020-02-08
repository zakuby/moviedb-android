package org.themoviedb.utils

import java.util.*

const val MOVIE_DATA = "movie"

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