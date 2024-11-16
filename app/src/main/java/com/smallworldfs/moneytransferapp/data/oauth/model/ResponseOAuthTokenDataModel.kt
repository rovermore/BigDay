package com.smallworldfs.moneytransferapp.data.oauth.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.utils.LONG_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class ResponseOAuthTokenDataModel(
    @SerializedName("access_token")
    var accessToken: String = STRING_EMPTY,
    @SerializedName("token_type")
    var tokenType: String = STRING_EMPTY,
    @SerializedName("expires_in")
    var expiresIn: Long = LONG_ZERO
) {
    fun getOAuthToken(): String {
        return "$tokenType $accessToken"
    }
}
