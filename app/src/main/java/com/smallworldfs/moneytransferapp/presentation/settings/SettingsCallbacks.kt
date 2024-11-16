package com.smallworldfs.moneytransferapp.presentation.settings

interface SettingsCallbacks {
    fun onBackButtonPressed()

    fun onSettingsItemClicked(url: String)

    fun onBiometricSwitchToggle(isChecked: Boolean)

    fun onNotificationSwitchToggle(isChecked: Boolean)

    fun onChangePasscodeSwitchToggle(isChecked: Boolean)

    fun onClosedIconClicked()
}
