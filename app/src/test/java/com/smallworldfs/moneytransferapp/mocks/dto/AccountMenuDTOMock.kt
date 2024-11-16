package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountMenuDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountMenuItemDTO

object AccountMenuDTOMock {
    private val accountMenuDTO1 = AccountMenuItemDTO(
        "block",
        "1",
        "coasas",
        "descripcion",
        true,
        2,
        4
    )

    private val accountMenuDTO2 = AccountMenuItemDTO(
        "block",
        "2",
        "coasas",
        "descripcion",
        true,
        2,
        4
    )

    private val accountMenuDTO3 = AccountMenuItemDTO(
        "block",
        "3",
        "coasas",
        "descripcion",
        true,
        2,
        4
    )

    private val accountMenuDTO4 = AccountMenuItemDTO(
        "block",
        "4",
        "coasas",
        "descripcion",
        true,
        2,
        4
    )
    val accountMenuDTO = AccountMenuDTO(
        mutableListOf(accountMenuDTO1, accountMenuDTO2),
        mutableListOf(accountMenuDTO3, accountMenuDTO4)
    )
}
