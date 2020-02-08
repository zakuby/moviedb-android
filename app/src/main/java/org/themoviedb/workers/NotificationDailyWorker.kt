package org.themoviedb.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.preference.PreferenceManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.themoviedb.R
import org.themoviedb.ui.main.MainActivity
import org.themoviedb.utils.IWorkerFactory
import org.themoviedb.utils.NOTIFICATION_CHANNEL_ID
import org.themoviedb.utils.NOTIFICATION_ID
import javax.inject.Inject
import javax.inject.Provider

class NotificationDailyWorker (ctx: Context, params: WorkerParameters) : Worker(ctx, params)  {

    private fun getContentIntent(): PendingIntent? {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            return@run getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    private fun createNotification() {
        val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText(applicationContext.getString(R.string.notify_daily_content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getContentIntent())
            .setAutoCancel(true)

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            with(applicationContext) {

                val name = getString(R.string.pref_notify_daily_reminder_name)
                val descriptionText = getString(R.string.pref_notify_daily_reminder_summary)
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
        with(NotificationManagerCompat.from(applicationContext)){
            notify(NOTIFICATION_ID, builder.build())
        }
    }



    override fun doWork(): Result {
        val prefManager = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify = prefManager.getBoolean(
            applicationContext.getString(R.string.pref_key_notify_daily_reminder),
            false
        )

        if (shouldNotify) createNotification()

        return Result.success()
    }

    class Factory @Inject constructor(
        private val context: Provider<Context>
    ) : IWorkerFactory<NotificationDailyWorker> {
        override fun create(params: WorkerParameters): NotificationDailyWorker = NotificationDailyWorker(context.get(), params)
    }
}