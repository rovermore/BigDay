package com.smallworldfs.moneytransferapp.presentation.settings.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class SettingsUIModel(
    val appInfo: AppInfo = AppInfo(),
    val appSettings: AppSettings = AppSettings(),
    val userInfoDTO: UserInfo = UserInfo()
)

data class AppInfo(
    val appVersion: String = STRING_EMPTY,
    val contactInfo: List<ContactInfo> = emptyList()
)

data class ContactInfo(
    val id: String,
    val title: String,
    val url: String
)

data class AppSettings(
    val notifications: Boolean = true
)

data class UserInfo(
    val isLimitedUser: Boolean = false,
    val country: String = STRING_EMPTY,
    val email: String = STRING_EMPTY
)
