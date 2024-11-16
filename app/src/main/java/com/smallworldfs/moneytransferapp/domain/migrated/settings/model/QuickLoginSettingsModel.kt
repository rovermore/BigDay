package com.smallworldfs.moneytransferapp.domain.migrated.settings.model

data class QuickLoginSettingsModel(
    val biometricsVisible: Boolean,
    val biometricsActive: Boolean,
    val passCodeVisible: Boolean,
    val passCodeActive: Boolean,
    val changePassCodeVisible: Boolean,
)
