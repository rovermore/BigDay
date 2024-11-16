package com.smallworldfs.moneytransferapp.domain.migrated.settings.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.SettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.repository.SettingsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.usertoken.repository.local.UserTokenLocal
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userDataRepository: UserDataRepository,
    private val userTokenLocal: UserTokenLocal
) {

    fun getSettings(): OperationResult<SettingsDTO, Error> =
        settingsRepository.getSettings().mapFailure {
            if (it is Error.Unauthorized) {
                clearUser()
                clearUserToken()
            }
            return Failure(it)
        }

    fun setNotificationsState(enable: Boolean) {
        settingsRepository.setNotificationsState(enable)
    }

    private fun clearUser() {
        userDataRepository.deleteLoggedUser()
        userDataRepository.deletePassCode()
        userDataRepository.deletePassword()
    }

    private fun clearUserToken() = userTokenLocal.clearUserToken()

    fun getQuickLoginSettings() = userDataRepository.getQuickLoginSettings()

    fun setBiometricsEnabled(): OperationResult<QuickLoginSettingsDTO, Error> {
        return userDataRepository.getQuickLoginSettings().map { quickLoginSettingsDTO ->
            return quickLoginSettingsDTO.activateBiometrics().map {
                return userDataRepository.saveQuickLoginSettings(it)
            }
        }
    }

    fun setBiometricsDisabled(): OperationResult<QuickLoginSettingsDTO, Error> {
        return userDataRepository.getQuickLoginSettings().map { quickLoginSettingsDTO ->
            return quickLoginSettingsDTO.deactivateBiometrics().map {
                return userDataRepository.saveQuickLoginSettings(it)
            }
        }
    }

    fun setPasscodeDisabled(): OperationResult<QuickLoginSettingsDTO, Error> {
        return userDataRepository.getQuickLoginSettings().map { quickLoginSettingsDTO ->
            return quickLoginSettingsDTO.deactivatePassCode().map {
                return userDataRepository.saveQuickLoginSettings(it)
            }
        }
    }
}
