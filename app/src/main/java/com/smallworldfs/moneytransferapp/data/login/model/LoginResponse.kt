package com.smallworldfs.moneytransferapp.data.login.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val data: Data,
) {

    data class Data(
        val user: UserResponse
    ) {
        data class UserResponse(
            val id: Int,
            val uuid: String?,
            val email: String?,
            val name: String?,
            val surname: String?,
            val country: String?,
            val status: String?,
            @SerializedName("user_token")
            val userToken: String?,
            val emailFraud: Boolean?
        )
    }
}
