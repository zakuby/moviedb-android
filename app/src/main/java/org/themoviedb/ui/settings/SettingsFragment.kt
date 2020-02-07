package org.themoviedb.ui.settings

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import org.themoviedb.R
import org.themoviedb.notification.NotificationDailyWorker
import org.themoviedb.utils.WORKER_DAILY_TAG
import org.themoviedb.utils.getDelayNotification
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.milliseconds


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

    private fun initLanguagePrefs(){
        val  languagePreference = findPreference<ListPreference>(getString(R.string.pref_key_language))
        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            updateLanguage(newValue as String)
            return@setOnPreferenceChangeListener true
        }

    }

    private fun updateLanguage(lang: String){
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val configuration: Configuration = resources.configuration
        val displayMetrics = resources.displayMetrics
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requireContext().createConfigurationContext(configuration)
        } else {
            resources.updateConfiguration(configuration, displayMetrics)
        }
        requireActivity().recreate()
    }


    private fun initNotifyReleaseReminderPrefs(){
        val notificationPreference = findPreference<SwitchPreference>(getString(R.string.pref_key_notify_release_reminder))
        notificationPreference?.setOnPreferenceChangeListener { _, newValue ->
            val value = newValue as Boolean
            //TODO: Set notification daily WorkManager
            return@setOnPreferenceChangeListener true
        }
    }


    private fun initNotifyDailyReminderPrefs(){
        val notificationPreference = findPreference<SwitchPreference>(getString(R.string.pref_key_notify_daily_reminder))
        notificationPreference?.setOnPreferenceChangeListener { _, _ ->
            enqueueDailyReminderWorker()
            return@setOnPreferenceChangeListener true
        }
    }
    private fun enqueueDailyReminderWorker(){
        val work = PeriodicWorkRequestBuilder<NotificationDailyWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(getDelayNotification(), TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(WORKER_DAILY_TAG, ExistingPeriodicWorkPolicy.REPLACE, work)
    }


}