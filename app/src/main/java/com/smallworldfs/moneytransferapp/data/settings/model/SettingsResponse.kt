package com.smallworldfs.moneytransferapp.data.settings.model

import com.google.gson.annotations.SerializedName

data class SettingsResponse(
    @SerializedName("data")
    var settingsInformation: List<SettingsInformation>,
    var msg: String
)

data class SettingsInformation(
    val id: String,
    val title: String,
    val url: String
)
