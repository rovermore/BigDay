package com.smallworldfs.moneytransferapp.data.operations.model

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import javax.inject.Inject

class OtpDTOMapper @Inject constructor() {

    fun map(response: OTPResponse): OperationResult<OtpDTO, Error> {
        response.data?.let {
            return if (isDataProcessable(response.data)) {
                Success(
                    OtpDTO(
                        it.user!!.uuid!!,
                        it.user!!.status!!,
                        it.otp!!.factorType!!,
                        it.otp.id!!,
                        it.otp.retries!!
                    )
                )
            } else Failure(Error.UncompletedOperation("Error mapping OTP"))
        } ?: return Failure(Error.UncompletedOperation("Error mapping OTP"))
    }

    private fun isDataProcessable(data: OTPResponse.Data) = (
        data.user != null && data.otp != null && data.user.uuid != null &&
            data.user.status != null && data.otp.factorType != null &&
            data.otp.id != null && data.otp.retries != null
        )
}
