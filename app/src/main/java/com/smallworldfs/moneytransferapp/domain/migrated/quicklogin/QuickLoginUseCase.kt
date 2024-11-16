package com.smallworldfs.moneytransferapp.domain.migrated.quicklogin

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class QuickLoginUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
) {

    fun setBiometricsEnabled(): OperationResult<Boolean, Error> {
        return userDataRepository.getQuickLoginSettings().map { quickLoginSettingsDTO ->
            return quickLoginSettingsDTO.activateBiometrics().map {
                return userDataRepository.saveQuickLoginSettings(it).map { true }
            }
        }
    }

    fun setBiometricsDisabled(): OperationResult<Boolean, Error> {
        return userDataRepository.getQuickLoginSettings().map { quickLoginSettingsDTO ->
            return quickLoginSettingsDTO.deactivateBiometrics().map {
                return userDataRepository.saveQuickLoginSettings(it).map { true }
            }
        }
    }

    fun getUserEmail() = userDataRepository.getLoggedUser().map { it.email }
}
