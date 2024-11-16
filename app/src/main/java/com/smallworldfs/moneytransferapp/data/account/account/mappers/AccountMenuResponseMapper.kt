package com.smallworldfs.moneytransferapp.data.account.account.mappers

import com.smallworldfs.moneytransferapp.data.account.account.model.AccountMenuResponse
import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountMenuDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountMenuItemDTO
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class AccountMenuResponseMapper @Inject constructor() {

    fun map(accountMenuResponse: AccountMenuResponse, isFreeUser: Boolean = false): AccountMenuDTO {
        val accountItemBlockList = mutableListOf<AccountMenuItemDTO>()
        val accountItemRowList = mutableListOf<AccountMenuItemDTO>()
        accountMenuResponse.data.blocks.map {
            accountItemBlockList.add(
                AccountMenuItemDTO(
                    type = it.type ?: STRING_EMPTY,
                    position = it.position ?: STRING_EMPTY,
                    title = it.title ?: STRING_EMPTY,
                    description = it.description ?: STRING_EMPTY,
                    active = it.active ?: false,
                    numInfo = it.numInfo ?: INT_ZERO,
                    numNewInfo = it.numNewInfo ?: INT_ZERO
                )
            )
        }

        if (isFreeUser) {
            accountMenuResponse.data.limitedRows.map {
                accountItemRowList.add(
                    AccountMenuItemDTO(
                        type = it.type ?: STRING_EMPTY,
                        position = it.position ?: STRING_EMPTY,
                        title = it.title ?: STRING_EMPTY,
                        description = it.description ?: STRING_EMPTY,
                        active = it.active ?: false,
                        numInfo = it.numInfo ?: INT_ZERO,
                        numNewInfo = it.numNewInfo ?: INT_ZERO
                    )
                )
            }
        } else {
            accountMenuResponse.data.rows.map {
                accountItemRowList.add(
                    AccountMenuItemDTO(
                        type = it.type ?: STRING_EMPTY,
                        position = it.position ?: STRING_EMPTY,
                        title = it.title ?: STRING_EMPTY,
                        description = it.description ?: STRING_EMPTY,
                        active = it.active ?: false,
                        numInfo = it.numInfo ?: INT_ZERO,
                        numNewInfo = it.numNewInfo ?: INT_ZERO
                    )
                )
            }
        }

        return AccountMenuDTO(accountItemBlockList, accountItemRowList)
    }
}
