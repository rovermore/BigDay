package com.smallworldfs.moneytransferapp.presentation.account.account.model

import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class AccountMenuUIModel(
    val blocks: MutableList<AccountMenuItemUIModel> = mutableListOf(),
    val rows: MutableList<AccountMenuItemUIModel> = mutableListOf()
)
data class AccountMenuItemUIModel(
    val type: String = STRING_EMPTY,
    val position: String = STRING_EMPTY,
    val title: String = STRING_EMPTY,
    val description: String = STRING_EMPTY,
    val active: Boolean = false,
    val numInfo: Int = INT_ZERO,
    val numNewInfo: Int = INT_ZERO,
    val icon: MenuIcon = MenuIcon.None
)

sealed class MenuIcon {
    data class Icon(val resourceId: Int) : MenuIcon()
    object None : MenuIcon()
}
