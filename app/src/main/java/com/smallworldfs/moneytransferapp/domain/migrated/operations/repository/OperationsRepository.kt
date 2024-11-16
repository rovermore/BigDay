package com.smallworldfs.moneytransferapp.domain.migrated.operations.repository

import com.smallworldfs.moneytransferapp.data.operations.model.OtpDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.operations.model.IntegrityDTO

interface OperationsRepository {

    fun validateOTP(otp: String, operationId: String): OperationResult<Boolean, Error>
    fun sendSMSOTP(phone: String, countryCode: String, nonce: String, integrityToken: String): OperationResult<OtpDTO, Error>
    fun resendSMSOTP(operationId: String, phone: String, countryCode: String, nonce: String, integrityToken: String): OperationResult<OtpDTO, Error>
    fun sendEmailOTP(nonce: String, integrityToken: String): OperationResult<OtpDTO, Error>
    fun getIntegrityDTO(operation: String): OperationResult<IntegrityDTO, Error>
}
