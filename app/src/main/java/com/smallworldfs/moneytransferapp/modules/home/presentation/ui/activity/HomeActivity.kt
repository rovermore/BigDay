package com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.contentsquare.android.Contentsquare
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ECommerceEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.compose.widgets.CountryListType
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.modules.home.presentation.navigator.HomeNavigator
import com.smallworldfs.moneytransferapp.presentation.home.HomeActivityCallbacks
import com.smallworldfs.moneytransferapp.presentation.home.HomeActivityLayout
import com.smallworldfs.moneytransferapp.presentation.home.HomeViewModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.SamsungMemLeak
import com.smallworldfs.moneytransferapp.utils.launchInAppReview
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : GenericActivity() {

    companion object {
        const val PROMO_REQUEST_CODE = 101
        const val SHOW_CHECKOUT_DIALOG_EXTRA = "SHOW_CHECKOUT_DIALOG_EXTRA"
        const val TRANSACTION_DATA = "TRANSACTION_DATA_EXTRA"
        const val CHECKOUT_DATA = "CHECKOUT_DATA_EXTRA"
        const val SHOW_SEND_TO_TAB_EXTRA = "SHOW_SEND_TO_TAB_EXTRA"
    }

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeActivityLayout(
                fragmentManager = supportFragmentManager,
                callbacks = HomeActivityCallbacks(
                    registerEvent = { eventAction, eventLabel, hierarchy -> registerEvent(eventAction, eventLabel, hierarchy) },
                    trackScreen = { screenName -> trackScreen(screenName) },
                    navigateToWelcomeActivity = { HomeNavigator.navigateToWelcomeActivity(this) },
                    navigateToMarketingPreferences = { title, gdprValue -> HomeNavigator.navigateToSettingsMarketingPreferences(this@HomeActivity, title, gdprValue) },
                    payNow = { mtn -> HomeNavigator.navigateToPayNowActivity(this@HomeActivity, mtn) },
                    showTransactionDetails = { transaction -> HomeNavigator.navigateToTransferDetails(this@HomeActivity, transaction) },
                    closeCheckoutDialog = { isTransactionSuccessful -> if (isTransactionSuccessful) homeViewModel.checkEmailValidated() },
                    launchInAppReview = { this.launchInAppReview() },
                    registerBrazeEvent = { trackEvent(BrazeEvent(BrazeEventName.PRIME_FOR_PUSH.value, HashMap<String, String>(), BrazeEventType.ACTION)) },
                ),
            )
        }
        observeFlows()
    }

    fun changeOriginCountryFromSendToFragment(homeActivityCountrySelectionCallback: HomeActivityCountrySelectionCallback) {
        homeViewModel.setSelectionCallback(homeActivityCountrySelectionCallback)
        homeViewModel.updateCountryListType(CountryListType.SendMoneyFrom)
        homeViewModel.showBottomSheet()
    }

    fun changeDestinationCountryFromSendToFragment(homeActivityCountrySelectionCallback: HomeActivityCountrySelectionCallback) {
        homeViewModel.setSelectionCallback(homeActivityCountrySelectionCallback)
        homeViewModel.updateCountryListType(CountryListType.SendMoneyTo)
        homeViewModel.showBottomSheet()
    }

    private fun observeFlows() {
        lifecycleScope.launch {
            homeViewModel.contentSquareStatus.collectLatest { userStatus ->
                if (userStatus == Constants.UserType.LIMITED) {
                    Contentsquare.optOut(this@HomeActivity)
                } else {
                    Contentsquare.optIn(this@HomeActivity)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SamsungMemLeak.onDestroy(applicationContext)
    }

    fun showTabIndicator(count: Int) {
        homeViewModel.showNotificationNumber(count)
    }

    private fun resetBehavior() {
        homeViewModel.resetBehavior(true)
    }

    fun switchToSendToTab() {
        homeViewModel.setPage(1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            Constants.REQUEST_CODES.NEW_BENEFICIARY -> {
                if (resultCode == RESULT_OK) {
                    homeViewModel.showNewBeneficiaryCreatedDialog(true)
                    resetBehavior()
                }
            }

            Constants.REQUEST_CODES.TRANSACTIONAL_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val showTransactionResponse = data!!.getBooleanExtra(SHOW_CHECKOUT_DIALOG_EXTRA, false)
                    val transactionCreated = data.getParcelableExtra<CreateTransactionResponse>(TRANSACTION_DATA)
                    val checkout = data.getParcelableExtra<Checkout>(CHECKOUT_DATA)
                    if (transactionCreated != null && showTransactionResponse) {
                        homeViewModel.showDialogCheckout(transactionCreated)
                    }
                    resetBehavior()
                    if ((checkout != null) && (checkout.transactionSummary != null) && (checkout.transactionDetails != null)) {
                        trackEvent(
                            ECommerceEvent(
                                homeViewModel.user.value.country.countries.firstOrNull()?.iso3 ?: "",
                                if (checkout.transactionSummary.country != null) checkout.transactionSummary.country else "",
                                if (checkout.transactionSummary.deliveryMethod != null) checkout.transactionSummary.deliveryMethod else "",
                                if (checkout.transactionDetails.totalToPay.toString() != "") checkout.transactionDetails.totalToPay.toString() else "",
                                if (checkout.transactionDetails.currencyOrigin != null) checkout.transactionDetails.currencyOrigin else "",
                                if (checkout.transactionSummary.currency != null) checkout.transactionSummary.currency else "",
                                "checkout_5",
                                "orderPlacedSuccessfully_transfer",
                                "5",
                                if (checkout.transactionSummary.promotionName != null) checkout.transactionSummary.promotionName else "",
                                if (checkout.transactionSummary.deliveryMethod != null) checkout.transactionSummary.deliveryMethod else "",
                                "",
                            ),
                        )
                    }
                }
            }

            Constants.REQUEST_CODES.MY_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val showTransferDetailsTab = data!!.getBooleanExtra(
                        TransactionStatusDetailActivity.RETURN_INTENT_DATA, false,
                    )
                    val showSendToTab = data.getBooleanExtra(SHOW_SEND_TO_TAB_EXTRA, false)
                    if (showTransferDetailsTab) {
                        homeViewModel.setPage(1)
                    } else if (showSendToTab) {
                        homeViewModel.setPage(0)
                    }
                }
            }

            Constants.REQUEST_CODES.PAY_NOW_ACTIVITY_REQUEST_CODE,
            Constants.REQUEST_CODES.TRANSFER_DETAILS_REQUEST_CODE -> homeViewModel.checkEmailValidated()

            Constants.REQUEST_CODES.BENEFICIARY_LIST -> if (resultCode == Constants.RESULT_CODES.BENEFICIARY_UPDATED) resetBehavior()
        }
    }

    private fun registerEvent(eventAction: String, eventLabel: String, hierarchy: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.DASHBOARD.value,
                eventAction,
                eventLabel,
                getHierarchy(hierarchy),
                "",
                "",
                "",
                "",
                "",
                "",
            ),
        )
    }
}
