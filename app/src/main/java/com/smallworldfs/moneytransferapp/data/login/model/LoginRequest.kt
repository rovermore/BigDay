package com.smallworldfs.moneytransferapp.data.login.model

data class LoginRequest(
    var email: String,
    var password: String,
    var country: String
)
