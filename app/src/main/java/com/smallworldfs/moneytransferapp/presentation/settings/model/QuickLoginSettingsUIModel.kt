package com.smallworldfs.moneytransferapp.presentation.settings.model

data class QuickLoginSettingsUIModel(
    val biometricsVisible: Boolean = false,
    val biometricsActive: Boolean = false,
    val passCodeVisible: Boolean = false,
    val passCodeActive: Boolean = false,
    val changePassCodeVisible: Boolean = false
)
