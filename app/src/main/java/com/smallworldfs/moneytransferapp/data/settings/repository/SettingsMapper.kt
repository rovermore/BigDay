package com.smallworldfs.moneytransferapp.data.settings.repository

import com.smallworldfs.moneytransferapp.data.settings.model.SettingsResponse
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.AppInfoDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.AppSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.ContactInfoDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.SettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.UserInfoDTO
import javax.inject.Inject

class SettingsMapper @Inject constructor() {
    fun map(
        settingsResponse: SettingsResponse,
        appVersion: String,
        isNotificationsEnabled: Boolean,
        isLimitedUser: Boolean,
        country: String,
        email: String
    ) =
        SettingsDTO(
            AppInfoDTO(
                appVersion,
                settingsResponse.settingsInformation.map {
                    ContactInfoDTO(
                        it.id,
                        it.title,
                        it.url,
                    )
                },
            ),
            AppSettingsDTO(isNotificationsEnabled),
            UserInfoDTO(isLimitedUser, country, email),
        )
}
