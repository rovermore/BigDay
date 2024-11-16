package com.smallworldfs.moneytransferapp.base.migrations

import android.content.Context
import android.content.SharedPreferences
import androidx.biometric.BiometricManager
import com.smallworldfs.moneytransferapp.data.userdata.local.UserDataLocalDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.recover
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class QuickLoginSettingsMigration @Inject constructor(
    private val context: Context,
    private val userDataLocalDatasource: UserDataLocalDatasource,
    private val userDataRepository: UserDataRepository,
    private val sharedPreferences: SharedPreferences
) : Migration {

    override fun execute() {
        if (!sharedPreferences.getBoolean("QuickLoginMigration", false)) {
            val biometricsAvailable = BiometricManager.from(context)
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
            val biometricsEnabled = userDataLocalDatasource.isFingerPrintEnabled().map { true }.recover { false }
            val passCodeEnabled = userDataLocalDatasource.retrievePassCode().map { true }.recover { false }
            val settingsDTO = QuickLoginSettingsDTO(biometricsAvailable, biometricsEnabled, passCodeEnabled)
            userDataRepository.saveQuickLoginSettings(settingsDTO).peek {
                sharedPreferences.edit().putBoolean("QuickLoginMigration", true).apply()
            }
        }
    }
}
