package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.operations.model.OTPResponse

object OTPResponseMock {

    private val otp = OTPResponse.Data.OTP("id", "factorType", 1)
    private val user = OTPResponse.Data.User("id", "uuid", "status", false)
    private val data = OTPResponse.Data(user, otp)

    val otpResponse = OTPResponse(data)
}
