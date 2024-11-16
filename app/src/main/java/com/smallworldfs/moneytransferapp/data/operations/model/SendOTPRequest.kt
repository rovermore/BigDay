package com.smallworldfs.moneytransferapp.data.operations.model

import com.smallworldfs.moneytransferapp.data.auth.oauth.model.Integrity

data class SendOTPRequest(
    val factorType: String,
    val profile: Profile,
    val integrity: Integrity
)

sealed class Profile {
    data class SMSProfile(val countryCode: String, val phoneNumber: String) : Profile()
    data class EmailProfile(val email: String) : Profile()
}
