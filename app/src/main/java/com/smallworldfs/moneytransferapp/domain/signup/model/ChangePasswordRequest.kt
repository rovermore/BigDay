package com.smallworldfs.moneytransferapp.domain.signup.model

class ChangePasswordRequest(
    password: String,
    newPassword: String,
    userId: String,
    userToken: String
) : HashMap<String, String>() {

    init {
        put("password", password)
        put("newPassword", newPassword)
        put("userId", userId)
        put("userToken", userToken)
    }
}
