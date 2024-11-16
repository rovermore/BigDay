package com.smallworldfs.moneytransferapp.data.operations.model

data class OTPResponse(val data: Data?) {
    data class Data(val user: User?, val otp: OTP?) {
        data class User(val id: String?, val uuid: String?, val status: String?, val validatedEmail: Boolean?)
        data class OTP(val id: String?, val factorType: String?, val retries: Int?)
    }
}
