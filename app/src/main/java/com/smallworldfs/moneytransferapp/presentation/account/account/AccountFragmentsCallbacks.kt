package com.smallworldfs.moneytransferapp.presentation.account.account

import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuItemUIModel

interface AccountFragmentsCallbacks {
    fun onItemClicked(item: AccountMenuItemUIModel)
    fun showTabIndicator(notifications: Int)
    fun onLogoutCompleted()
    fun showLogoutDialog(onDialogAccept: Action)
    fun onUserClicked(userStatus: String)
}
