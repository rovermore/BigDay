package com.smallworldfs.moneytransferapp.data.login.model

import com.google.gson.annotations.SerializedName

data class SoftRegisterResponse(
    val data: Data
)

data class Data(
    val user: User
)

data class User(
    val id: Int,
    val uuid: String,
    val email: String,
    val name: String?,
    val surname: String?,
    val country: String,
    val status: String,
    @SerializedName("user_token")
    val userToken: String,
    val appToken: String?,
    val emailFraud: Boolean
)
