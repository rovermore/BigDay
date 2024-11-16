package com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class ContactSupportDTO(
    val phone: String = STRING_EMPTY,
    val email: String = STRING_EMPTY,
    val isLimitedUser: Boolean = false
)
