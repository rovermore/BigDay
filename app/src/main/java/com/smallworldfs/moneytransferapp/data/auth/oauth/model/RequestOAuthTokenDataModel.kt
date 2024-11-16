package com.smallworldfs.moneytransferapp.data.auth.oauth.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.api.Api

data class RequestOAuthTokenDataModel(
    val integrity: Integrity,
    @SerializedName("client_id") val clientId: String = Api.API_USER.API_CLIENT_ID,
    @SerializedName("client_secret") val clientSecret: String = Api.API_USER.API_CLIENT_SECRET,
    @SerializedName("grant_type") val grantType: String = Api.API_USER.API_GRANT_TYPE,
    @SerializedName("password") val password: String = Api.API_USER.API_CLIENT_PASSWORD,
    @SerializedName("username") val username: String = Api.API_USER.API_CLIENT_USER,
)

data class Integrity(
    val nonce: String,
    val requestInfo: RequestInfo
)

data class RequestInfo(
    val signature: String
)
