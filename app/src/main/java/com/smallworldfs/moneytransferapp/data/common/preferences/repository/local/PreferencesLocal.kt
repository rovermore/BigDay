package com.smallworldfs.moneytransferapp.data.common.preferences.repository.local

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesLocal @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val ON_BOARD_SHOWED = "ONBOARD_SHOWED"
        private const val NOTIFICATIONS_ENABLED_PREFERENCE = "NOTIF_ENABLED"
        private const val QUICK_LOGIN_SETTINGS = "QUICK_LOGIN_SETTINGS"
    }

    fun onBoardShown(showed: Boolean) = sharedPreferences.edit().putBoolean(ON_BOARD_SHOWED, showed).apply()

    fun isOnBoardShown(): Boolean = sharedPreferences.getBoolean(ON_BOARD_SHOWED, false)

    fun isNotificationsEnabled(): Boolean = sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED_PREFERENCE, false)

    fun setNotificationsEnabled(enabled: Boolean) = sharedPreferences.edit().putBoolean(NOTIFICATIONS_ENABLED_PREFERENCE, enabled).apply()

    fun getQuickLoginSettings() = sharedPreferences.getString(QUICK_LOGIN_SETTINGS, null)

    fun setQuickLoginSettings(serializedSettings: String) = sharedPreferences.edit().putString(QUICK_LOGIN_SETTINGS, serializedSettings).apply()
}
