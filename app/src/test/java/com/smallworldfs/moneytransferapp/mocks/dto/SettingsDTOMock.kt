package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.AppInfoDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.AppSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.ContactInfoDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.SettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.UserInfoDTO

object SettingsDTOMock {

    private val userInfoDTO = UserInfoDTO(
        false,
        "es",
        "email"
    )

    private val appSettingsDTO = AppSettingsDTO(true)

    private val contactInfoDTO = ContactInfoDTO(
        "id",
        "title",
        "url"
    )

    private val appInfoDTO = AppInfoDTO("appVersion", listOf(contactInfoDTO, contactInfoDTO))

    val settingsDTO = SettingsDTO(
        appInfoDTO,
        appSettingsDTO,
        userInfoDTO
    )
}
