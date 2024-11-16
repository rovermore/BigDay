package com.smallworldfs.moneytransferapp.presentation.freeuser.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment
import com.smallworldfs.moneytransferapp.modules.home.presentation.navigator.HomeNavigator
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuItemUIModel
import com.smallworldfs.moneytransferapp.utils.Constants.ACCOUNT_SECTIONS_KEYS.CUSTOMER_SUPPORT
import com.smallworldfs.moneytransferapp.utils.Constants.ACCOUNT_SECTIONS_KEYS.OFFICES
import com.smallworldfs.moneytransferapp.utils.Constants.ACCOUNT_SECTIONS_KEYS.SETTINGS

class FreeUserFragment : GenericFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FreeUserLayout(
                    onLoginClick = { HomeNavigator.navigateToLoginActivity(requireActivity(), true) },
                    onSignUpClick = { HomeNavigator.navigateToSignUpActivity(requireActivity()) },
                    onAccountClick = { HomeNavigator.navigateToWelcomeActivity(requireActivity()) },
                    onItemClicked = { handleSectionClick(it) }
                )
            }
        }
    }

    private fun handleSectionClick(accountMenuItemUIModel: AccountMenuItemUIModel) {
        when (accountMenuItemUIModel.type) {
            CUSTOMER_SUPPORT -> HomeNavigator.navigateToContactSupportActivity(requireActivity())
            OFFICES -> HomeNavigator.navigateToOfficesActivity(requireActivity(), accountMenuItemUIModel.title)
            SETTINGS -> HomeNavigator.navigateToSettingsActivity(requireActivity())
        }
    }
}
