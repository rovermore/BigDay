package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.settings.model.SettingsInformation
import com.smallworldfs.moneytransferapp.data.settings.model.SettingsResponse

object SettingsResponseMock {

    private val settingsInformation = SettingsInformation(
        "id",
        "title",
        "url"
    )

    val settingsResponse = SettingsResponse(
        listOf(settingsInformation, settingsInformation),
        "msg"
    )
}
