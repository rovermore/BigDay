package com.smallworldfs.moneytransferapp.presentation.account.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import com.contentsquare.android.Contentsquare
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showDoubleActionGeneralDialog
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment
import com.smallworldfs.moneytransferapp.modules.home.presentation.navigator.HomeNavigator
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuItemUIModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : GenericFragment(), AccountFragmentsCallbacks {

    companion object {
        val SCREEN_NAME = "ACCOUNT_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AccountFragmentLayout(callbacks = this@AccountFragment)
            }
        }
    }

    override fun onItemClicked(item: AccountMenuItemUIModel) {
        val activity = requireActivity()
        when (item.type) {
            Constants.ACCOUNT_SECTIONS_KEYS.MY_BENEFICIARIES -> {
                registerEvent("click_my_beneficiaries")
                HomeNavigator.navigateToMyBeneficiariesActivity(activity)
            }
            Constants.ACCOUNT_SECTIONS_KEYS.MY_ACTIVITY -> {
                registerEvent("click_my_activity")
                HomeNavigator.navigateToMyActivityActivity(activity)
            }
            Constants.ACCOUNT_SECTIONS_KEYS.MY_DOCUMENTS -> {
                registerEvent("click_my_documents")
                HomeNavigator.navigateToMyDocumentsActivity(activity)
            }
            Constants.ACCOUNT_SECTIONS_KEYS.CUSTOMER_SUPPORT -> {
                registerEvent("click_customer_support")
                HomeNavigator.navigateToContactSupportActivity(activity)
            }
            Constants.ACCOUNT_SECTIONS_KEYS.OFFICES -> {
                registerEvent("click_offices")
                HomeNavigator.navigateToOfficesActivity(activity, item.title)
            }
            Constants.ACCOUNT_SECTIONS_KEYS.TRACKING_NUMBER -> {
                HomeNavigator.navigateToMTNActivity(activity)
            }
            Constants.ACCOUNT_SECTIONS_KEYS.SETTINGS -> {
                registerEvent("click_settings")
                HomeNavigator.navigateToSettingsActivity(activity)
            }
            Constants.ACCOUNT_SECTIONS_KEYS.MARKETING_PREF -> {
                registerEvent("click_marketing_preferences")
                HomeNavigator.navigateToSettingsMarketingPreferences(
                    activity,
                    item.title,
                    Constants.GDPR_VALUES.FROM_VIEW_ACCOUNT
                )
            }
        }
    }

    override fun showTabIndicator(notifications: Int) {
        (activity as HomeActivity).showTabIndicator(notifications)
    }

    override fun onLogoutCompleted() {
        HomeNavigator.navigateToLoginActivity(activity, true)
    }

    override fun showLogoutDialog(onDialogAccept: Action) {
        registerEvent("click_logout")
        val activity = requireActivity() as AppCompatActivity
        activity.showDoubleActionGeneralDialog(
            activity.getString(R.string.logout_dialog_title),
            activity.getString(R.string.logout_dialog_content),
            activity.getString(R.string.accept_text),
            {
                registerEvent("click_accept", ScreenName.END_SESSION_DIALOG.value)
                onDialogAccept()

                // Stop tracking ContentSquare Id and forget the id of the user
                Contentsquare.optOut(requireContext())
                Contentsquare.forgetMe()
            },
            activity.getString(R.string.cancel),
            {
                registerEvent("click_cancel", ScreenName.END_SESSION_DIALOG.value)
            },
            ScreenName.END_SESSION_DIALOG.value,
            positiveContentDescription = "ok_button",
            negativeContentDescription = "cancel_button"
        )
    }

    override fun onUserClicked(userStatus: String) {
        if (userStatus != Constants.UserType.LIMITED)
            registerEvent("click_own_name")
        HomeNavigator.navigateToProfileActivity(requireActivity())
    }

    private fun registerEvent(eventAction: String, hierarchy: String = STRING_EMPTY) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.DASHBOARD.value,
                eventAction,
                "",
                getHierarchy(hierarchy),
            ),
        )
    }
}
