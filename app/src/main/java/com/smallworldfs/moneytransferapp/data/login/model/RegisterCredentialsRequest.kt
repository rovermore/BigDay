package com.smallworldfs.moneytransferapp.data.login.model

import com.smallworldfs.moneytransferapp.data.auth.oauth.model.Integrity

data class RegisterCredentialsRequest(
    val appToken: String?,
    val uuid: String?,
    val email: String,
    val countryOrigin: String,
    val password: String,
    val state: String,
    val checkMarketing: Boolean,
    val checkPrivacy: Boolean,
    val checkTerms: Boolean,
    val integrity: Integrity
)
