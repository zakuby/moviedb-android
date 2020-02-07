package org.themoviedb.ui.settings

import android.os.Bundle
import android.view.Menu
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import org.themoviedb.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        initNotifyReleaseReminderPrefs()
        initNotifyDailyReminderPrefs()
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
        notificationPreference?.setOnPreferenceChangeListener { _, newValue ->
            val value = newValue as Boolean
            //TODO: Set notification daily WorkManager
            return@setOnPreferenceChangeListener true
        }
    }


}