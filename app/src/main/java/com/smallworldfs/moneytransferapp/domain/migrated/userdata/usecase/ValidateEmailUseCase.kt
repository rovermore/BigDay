package com.smallworldfs.moneytransferapp.domain.migrated.userdata.usecase

import com.smallworldfs.moneytransferapp.data.operations.model.OtpDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.operations.repository.OperationsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val operationsRepository: OperationsRepository,
) {
    private val OTP = "otp"

    fun sendEmail(): OperationResult<OtpDTO, Error> {
        return operationsRepository.getIntegrityDTO(OTP).map {
            return operationsRepository.sendEmailOTP(it.nonce, it.requestInfo.signature)
                .peek {
                    return Success(it)
                }
                .peekFailure {
                    return Failure(it)
                }
        }.peekFailure {
            return Failure(it)
        }
    }

    fun getUserEmail() = userDataRepository.getLoggedUser().map { it.email }
}
