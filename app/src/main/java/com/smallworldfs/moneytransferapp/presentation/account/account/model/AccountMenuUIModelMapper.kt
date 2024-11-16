package com.smallworldfs.moneytransferapp.presentation.account.account.model

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountMenuDTO
import com.smallworldfs.moneytransferapp.utils.Constants
import javax.inject.Inject

class AccountMenuUIModelMapper @Inject constructor() {
    fun map(accountMenuDTO: AccountMenuDTO): AccountMenuUIModel {
        val accountItemBlockList = mutableListOf<AccountMenuItemUIModel>()
        val accountItemRowList = mutableListOf<AccountMenuItemUIModel>()
        accountMenuDTO.blocks.map {
            accountItemBlockList.add(
                AccountMenuItemUIModel(
                    type = it.type,
                    position = it.position,
                    title = it.title,
                    description = it.description,
                    active = it.active,
                    numInfo = it.numInfo,
                    numNewInfo = it.numNewInfo,
                    icon = getIconForSection(it.type)
                )
            )
        }
        accountMenuDTO.rows.map {
            accountItemRowList.add(
                AccountMenuItemUIModel(
                    type = it.type,
                    position = it.position,
                    title = it.title,
                    description = it.description,
                    active = it.active,
                    numInfo = it.numInfo,
                    numNewInfo = it.numNewInfo,
                    icon = getIconForSection(it.type)
                )
            )
        }
        return AccountMenuUIModel(accountItemBlockList, accountItemRowList)
    }

    private fun getIconForSection(type: String): MenuIcon {
        return when (type.toUpperCase(Locale.current)) {
            Constants.ACCOUNT_SECTIONS_KEYS.MY_BENEFICIARIES ->
                MenuIcon.Icon(R.drawable.account_icn_mybeneficiaries)

            Constants.ACCOUNT_SECTIONS_KEYS.MY_ACTIVITY ->
                MenuIcon.Icon(R.drawable.account_icn_myactivity)

            Constants.ACCOUNT_SECTIONS_KEYS.REQUESTS ->
                MenuIcon.Icon(R.drawable.account_icn_requests)

            Constants.ACCOUNT_SECTIONS_KEYS.MY_DOCUMENTS ->
                MenuIcon.Icon(R.drawable.account_icn_mydocuments)

            Constants.ACCOUNT_SECTIONS_KEYS.CUSTOMER_SUPPORT ->
                MenuIcon.Icon(R.drawable.account_icn_customersupport)

            Constants.ACCOUNT_SECTIONS_KEYS.OFFICES ->
                MenuIcon.Icon(R.drawable.account_icn_offices)

            Constants.ACCOUNT_SECTIONS_KEYS.TRACKING_NUMBER ->
                MenuIcon.Icon(R.drawable.account_icn_checkmtn)

            Constants.ACCOUNT_SECTIONS_KEYS.SETTINGS ->
                MenuIcon.Icon(R.drawable.account_icn_settings)

            Constants.ACCOUNT_SECTIONS_KEYS.MARKETING_PREF ->
                MenuIcon.Icon(R.drawable.account_icn_marketingpreferences)

            else -> MenuIcon.None
        }
    }
}
