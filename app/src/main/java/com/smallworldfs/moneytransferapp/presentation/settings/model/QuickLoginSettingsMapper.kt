package com.smallworldfs.moneytransferapp.presentation.settings.model

import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import javax.inject.Inject

class QuickLoginSettingsMapper @Inject constructor() {
    fun map(dto: QuickLoginSettingsDTO): QuickLoginSettingsUIModel {
        val biometricsVisible = dto.state is QuickLoginSettingsDTO.BiometricDeviceQuickLoginSettingsState
        val biometricsActive = dto.biometricsActive
        val changePassCodeVisible = dto.changePassCodeVisible
        val passCodeVisible = dto.state is QuickLoginSettingsDTO.QuickLoginSettingsState
        val passCodeActive = dto.passcodeActive
        return QuickLoginSettingsUIModel(biometricsVisible, biometricsActive, passCodeVisible, passCodeActive, changePassCodeVisible)
    }
}
