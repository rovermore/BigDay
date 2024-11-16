package com.smallworldfs.moneytransferapp.data.account.profile.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class StateRequest(
    val type: String = STRING_EMPTY,
    val state: String = STRING_EMPTY,
    val country: String = STRING_EMPTY,
)
