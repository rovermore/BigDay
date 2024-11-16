package com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class StateDTO(
    val code: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    val logo: String = STRING_EMPTY,
    val active: Boolean = false,
)
