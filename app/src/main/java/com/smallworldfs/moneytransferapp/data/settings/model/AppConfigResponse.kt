package com.smallworldfs.moneytransferapp.data.settings.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class AppConfigResponse(
    var msg: String? = STRING_EMPTY,
    var cache: String? = STRING_EMPTY,
    @SerializedName("vmin_android") var minVersion: String? = STRING_EMPTY,
    var brazeEnabled: Boolean = true
)
