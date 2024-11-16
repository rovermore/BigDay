package com.smallworldfs.moneytransferapp.data.account.profile.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class EditProfileFormRequest(
    val userToken: String = STRING_EMPTY,
    val userId: String = STRING_EMPTY
)
