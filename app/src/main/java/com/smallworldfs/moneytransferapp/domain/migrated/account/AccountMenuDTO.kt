package com.smallworldfs.moneytransferapp.domain.migrated.account

data class AccountMenuDTO(
    val blocks: MutableList<AccountMenuItemDTO> = mutableListOf(),
    val rows: MutableList<AccountMenuItemDTO> = mutableListOf()
)
