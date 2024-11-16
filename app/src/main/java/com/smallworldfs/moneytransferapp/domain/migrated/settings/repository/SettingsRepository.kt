package com.smallworldfs.moneytransferapp.domain.migrated.settings.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.AppConfigDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.SettingsDTO
import com.smallworldfs.moneytransferapp.modules.settings.domain.model.SettingsServer

interface SettingsRepository {
    fun getSettings(): OperationResult<SettingsDTO, Error>
    fun setNotificationsState(enable: Boolean)
    fun getAppConfig(): OperationResult<AppConfigDTO, Error>
    fun setOnboardingShown(shown: Boolean)
    fun isOnBoardShown(): OperationResult<Boolean, Error>
    fun saveSettings(country: String, userId: String, userToken: String, accept: String): OperationResult<SettingsServer, Error>
}
