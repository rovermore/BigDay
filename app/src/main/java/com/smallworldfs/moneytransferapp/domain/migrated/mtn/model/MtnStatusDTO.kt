package com.smallworldfs.moneytransferapp.domain.migrated.mtn.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class MtnStatusDTO(
    val status: String = STRING_EMPTY,
    val statusList: List<StatusDTO> = listOf(),
    val country: String = STRING_EMPTY,
    val mtn: String = STRING_EMPTY
)

data class StatusDTO(
    val id: String = STRING_EMPTY,
    val title: String = STRING_EMPTY,
    val status: String = STRING_EMPTY
)
