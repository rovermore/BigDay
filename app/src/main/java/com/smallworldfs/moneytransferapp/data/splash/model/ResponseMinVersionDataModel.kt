package com.smallworldfs.moneytransferapp.data.splash.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class ResponseMinVersionDataModel(
    var msg: String? = STRING_EMPTY,
    var cache: String? = STRING_EMPTY,
    @SerializedName("vmin_ios") var vMiniOS: String? = STRING_EMPTY,
    @SerializedName("vmin_android") var vMinAndroid: String? = STRING_EMPTY
)
