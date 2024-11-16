package com.smallworldfs.moneytransferapp.data.login.model

data class LimitedLoginResponse(
    val data: Data,
) {

    data class Data(
        val user: User
    ) {
        data class User(
            val id: String,
            val uuid: String,
            val email: String?,
            val name: String?,
            val surname: String?,
            val country: String,
            val status: String,
            val user_token: String,
            val appToken: String,
            val emailFraud: String?
        )
    }
}
