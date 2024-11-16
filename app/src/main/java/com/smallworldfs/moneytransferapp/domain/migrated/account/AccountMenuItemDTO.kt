package com.smallworldfs.moneytransferapp.domain.migrated.account

import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class AccountMenuItemDTO(
    val type: String = STRING_EMPTY,
    val position: String = STRING_EMPTY,
    val title: String = STRING_EMPTY,
    val description: String = STRING_EMPTY,
    val active: Boolean = false,
    val numInfo: Int = INT_ZERO,
    val numNewInfo: Int = INT_ZERO
)
