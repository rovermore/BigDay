package com.smallworldfs.moneytransferapp.domain.migrated.settings.model

data class SettingsDTO(
    val appInfo: AppInfoDTO,
    val appSettings: AppSettingsDTO,
    val userInfoDTO: UserInfoDTO
)

data class AppInfoDTO(
    val appVersion: String,
    val contactInfo: List<ContactInfoDTO>
)

data class ContactInfoDTO(
    val id: String,
    val title: String,
    val url: String,
)

data class AppSettingsDTO(
    val notifications: Boolean = true
)

data class UserInfoDTO(
    val isLimitedUser: Boolean,
    val country: String,
    val email: String
)
