package com.smallworldfs.moneytransferapp.domain.migrated.passcode.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PassCodeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class PassCodeUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {

    fun savePasscode(passcode: CharArray): OperationResult<Boolean, Error> {
        val passCodeDTO = PassCodeDTO(code = passcode)
        return userDataRepository.getQuickLoginSettings().map { quickLoginSettingsDTO ->
            return quickLoginSettingsDTO.activatePassCode().map {
                return userDataRepository.saveQuickLoginSettings(it).map {
                    return userDataRepository.setPassCode(passCodeDTO)
                }
            }
        }
    }

    fun resetPasscode(): OperationResult<Boolean, Error> {
        return userDataRepository.getQuickLoginSettings().map { quickLoginSettingsDTO ->
            return quickLoginSettingsDTO.deactivatePassCode().map {
                return userDataRepository.saveQuickLoginSettings(it).map {
                    return userDataRepository.deletePassCode().map { true }
                }
            }
        }
    }

    fun validatePassCode(passCodeDTO: PassCodeDTO): OperationResult<Boolean, Error> {
        return userDataRepository.getQuickLoginSettings().map { settingsDTO ->
            return if (settingsDTO.passcodeActive) {
                return userDataRepository.getPassCode()
                    .map {
                        return if (it == passCodeDTO) {
                            Success(true)
                        } else {
                            Failure(Error.OperationCompletedWithError())
                        }
                    }
            } else Failure(Error.UnsupportedOperation("Passcode is not active"))
        }
    }
}
