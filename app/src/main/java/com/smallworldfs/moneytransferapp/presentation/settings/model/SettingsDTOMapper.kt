package com.smallworldfs.moneytransferapp.presentation.settings.model

import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.SettingsDTO
import javax.inject.Inject

class SettingsDTOMapper @Inject constructor() {
    fun map(settingsDTO: SettingsDTO): SettingsUIModel {
        return SettingsUIModel(
            AppInfo(
                settingsDTO.appInfo.appVersion,
                settingsDTO.appInfo.contactInfo.map {
                    ContactInfo(
                        it.id,
                        it.title,
                        it.url
                    )
                }
            ),
            AppSettings(settingsDTO.appSettings.notifications),
            UserInfo(
                settingsDTO.userInfoDTO.isLimitedUser,
                settingsDTO.userInfoDTO.country,
                settingsDTO.userInfoDTO.email
            )
        )
    }
}
