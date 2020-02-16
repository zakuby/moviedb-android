package org.themoviedb.ui.settings

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.*
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import org.themoviedb.R
import org.themoviedb.utils.WORKER_DAILY_TAG
import org.themoviedb.utils.WORKER_RELEASE_TAG
import org.themoviedb.utils.getDelayNextDay
import org.themoviedb.workers.NotificationDailyWorker
import org.themoviedb.workers.ReleaseReminderWorker
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat(), HasAndroidInjector {

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        initLanguagePrefs()
        initNotifyReleaseReminderPrefs()
        initNotifyDailyReminderPrefs()
    }

    private fun initLanguagePrefs() {
        val languagePreference = findPreference<ListPreference>(getString(R.string.pref_key_language))
        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            updateLanguage(newValue as String)
            return@setOnPreferenceChangeListener true
        }
    }

    @Suppress("DEPRECATION")
    private fun updateLanguage(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val configuration: Configuration = resources.configuration
        val displayMetrics = resources.displayMetrics
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            configuration.setLocale(locale)
            requireContext().createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, displayMetrics)
        }
        requireActivity().recreate()
    }

    private fun initNotifyReleaseReminderPrefs() {
        val notificationPreference = findPreference<SwitchPreference>(getString(R.string.pref_key_notify_release_reminder))
        notificationPreference?.setOnPreferenceChangeListener { _, _ ->
            enqueueReleaseReminderWorker()
            return@setOnPreferenceChangeListener true
        }
    }

    private fun initNotifyDailyReminderPrefs() {
        val notificationPreference = findPreference<SwitchPreference>(getString(R.string.pref_key_notify_daily_reminder))
        notificationPreference?.setOnPreferenceChangeListener { _, _ ->
            enqueueDailyReminderWorker()
            return@setOnPreferenceChangeListener true
        }
    }

    private fun enqueueReleaseReminderWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val work = PeriodicWorkRequestBuilder<ReleaseReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(getDelayNextDay(0), TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(WORKER_RELEASE_TAG, ExistingPeriodicWorkPolicy.REPLACE, work)
    }

    private fun enqueueDailyReminderWorker() {
        val work = PeriodicWorkRequestBuilder<NotificationDailyWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(getDelayNextDay(7), TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(WORKER_DAILY_TAG, ExistingPeriodicWorkPolicy.REPLACE, work)
    }
}