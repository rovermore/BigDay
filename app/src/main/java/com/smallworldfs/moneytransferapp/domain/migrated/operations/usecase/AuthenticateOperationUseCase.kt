package com.smallworldfs.moneytransferapp.domain.migrated.operations.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.operations.repository.OperationsRepository
import javax.inject.Inject

class AuthenticateOperationUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {

    fun validateOTP(otp: String, operationId: String): OperationResult<Boolean, Error> {
        return operationsRepository.validateOTP(otp, operationId)
    }
}
