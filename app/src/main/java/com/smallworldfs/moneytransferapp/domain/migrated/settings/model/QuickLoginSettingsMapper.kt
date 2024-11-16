package com.smallworldfs.moneytransferapp.domain.migrated.settings.model

import javax.inject.Inject

class QuickLoginSettingsMapper @Inject constructor() {

    fun map(dto: QuickLoginSettingsDTO): QuickLoginSettingsModel {
        val biometricsVisible = dto.state is QuickLoginSettingsDTO.BiometricDeviceQuickLoginSettingsState
        val biometricsActive = dto.biometricsActive
        val changePassCodeVisible = dto.changePassCodeVisible
        val passCodeVisible = dto.state is QuickLoginSettingsDTO.QuickLoginSettingsState
        val passCodeActive = dto.passcodeActive
        return QuickLoginSettingsModel(biometricsVisible, biometricsActive, passCodeVisible, passCodeActive, changePassCodeVisible)
    }
}
