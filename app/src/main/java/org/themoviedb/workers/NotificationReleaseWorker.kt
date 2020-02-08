package org.themoviedb.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import org.themoviedb.R
import org.themoviedb.data.local.models.Movie
import org.themoviedb.ui.main.MainActivity
import org.themoviedb.utils.IWorkerFactory
import org.themoviedb.utils.MOVIE_DATA
import org.themoviedb.utils.NOTIFICATION_CHANNEL_ID
import javax.inject.Inject
import javax.inject.Provider

class NotificationReleaseWorker(
    context: Context,
    params: WorkerParameters,
    private val gson: Gson
) : Worker(context, params) {

    private fun getMovieData(): Movie {
        val json = inputData.getString(MOVIE_DATA)
        return gson.fromJson(json, Movie::class.java)
    }

    private fun getContentIntent(): PendingIntent? =
            NavDeepLinkBuilder(applicationContext).setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.nav_main)
                .setDestination(R.id.detail_fragment)
                .setArguments(bundleOf(MOVIE_DATA to getMovieData()))
                .createPendingIntent()

    private fun createNotification() {
        val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getMovieData().title)
            .setContentText(applicationContext.getString(R.string.notify_release_content, getMovieData().title))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getContentIntent())
            .setAutoCancel(true)

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            with(applicationContext) {

                val name = getString(R.string.pref_notify_release_reminder_name)
                val descriptionText = getString(R.string.pref_notify_release_reminder_summary)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    name,
                    importance
                ).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(getMovieData().id, builder.build())
        }
    }

    override fun doWork(): Result {
        val prefManager = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify = prefManager.getBoolean(
            applicationContext.getString(R.string.pref_key_notify_release_reminder),
            false
        )

        if (shouldNotify) createNotification()

        return Result.success()
    }

    class Factory @Inject constructor(
        private val context: Provider<Context>,
        private val gson: Provider<Gson>
    ) : IWorkerFactory<NotificationReleaseWorker> {
        override fun create(params: WorkerParameters): NotificationReleaseWorker = NotificationReleaseWorker(context.get(), params, gson.get())
    }
}