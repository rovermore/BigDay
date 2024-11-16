package com.smallworldfs.moneytransferapp.presentation.home

import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.compose.colors.defaultTextColor
import com.smallworldfs.moneytransferapp.compose.dialogs.SWInfoDialog
import com.smallworldfs.moneytransferapp.compose.widgets.CountryListType
import com.smallworldfs.moneytransferapp.compose.widgets.FragmentContainer
import com.smallworldfs.moneytransferapp.compose.widgets.LoginToSendFooter
import com.smallworldfs.moneytransferapp.compose.widgets.PagerItem
import com.smallworldfs.moneytransferapp.compose.widgets.SWBottomSheetContentCountryList
import com.smallworldfs.moneytransferapp.compose.widgets.model.SWCountryListWithSectionsItemUIModel
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors
import com.smallworldfs.moneytransferapp.modules.login.domain.model.Gdpr
import com.smallworldfs.moneytransferapp.modules.tracking.ui.fragment.TrackingFragment
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.fragment.BeneficiaryListTouchListener
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.fragment.SendToFragment
import com.smallworldfs.moneytransferapp.presentation.account.account.AccountFragment
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.freeuser.account.FreeUserFragment
import com.smallworldfs.moneytransferapp.presentation.home.dialog.CheckoutDialog
import com.smallworldfs.moneytransferapp.presentation.home.dialog.GdprDialog
import com.smallworldfs.moneytransferapp.presentation.home.dialog.MarketingPreferencesDialog
import com.smallworldfs.moneytransferapp.presentation.home.dialog.ValidateEmailDialog
import com.smallworldfs.moneytransferapp.presentation.home.model.CheckoutDialogUIModel
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel
import com.smallworldfs.moneytransferapp.presentation.status.StatusFragment
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.coroutines.launch

@Composable
fun HomeActivityLayout(
    viewModel: HomeViewModel = viewModel(),
    fragmentManager: FragmentManager,
    callbacks: HomeActivityCallbacks
) {

    val isLimitedUser by viewModel.isLimitedUser.collectAsStateWithLifecycle()
    val destinationCountry by viewModel.selectedDestinationCountry.collectAsStateWithLifecycle()
    val originCountry by viewModel.selectedOriginCountry.collectAsStateWithLifecycle()
    val destinationCountryList by viewModel.filteredDestinationList.collectAsStateWithLifecycle()
    val originCountryList by viewModel.filteredOriginList.collectAsStateWithLifecycle()
    val recentCountriesUsed by viewModel.recentCountriesUsed.collectAsStateWithLifecycle()
    val shouldResetBehavior by viewModel.resetBehavior.collectAsStateWithLifecycle()
    val page by viewModel.setPage.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val countryListType by viewModel.countryListType.collectAsStateWithLifecycle()
    val gdprInfo by viewModel.showGDPRInfo.collectAsStateWithLifecycle()
    val sendEmailValidationDialog by viewModel.showSendEmailValidationDialog.collectAsStateWithLifecycle()
    val showEmailValidated by viewModel.showEmailValidated.collectAsStateWithLifecycle()
    val showMarketingPref by viewModel.showMarketingDialog.collectAsStateWithLifecycle()
    val checkoutDialogTransaction by viewModel.checkoutDialog.collectAsStateWithLifecycle()
    val notifications by viewModel.showNotificationNumber.collectAsStateWithLifecycle()
    val newBeneficiaryCreated by viewModel.newBeneficiaryCreated.collectAsStateWithLifecycle()
    val showAppRatingDialog by viewModel.showAppRatingDialog.collectAsStateWithLifecycle()
    val primeForPush by viewModel.primeForPush.collectAsStateWithLifecycle()
    val showBottomSheet by viewModel.showBottomSheet.collectAsStateWithLifecycle()

    LaunchedEffect(showAppRatingDialog) {
        if (showAppRatingDialog)
            callbacks.launchInAppReview()
    }

    LaunchedEffect(primeForPush) {
        if (primeForPush)
            callbacks.registerBrazeEvent()
    }

    Content(
        fragmentManager,
        isLimitedUser,
        destinationCountry,
        originCountry,
        gdprInfo,
        user,
        sendEmailValidationDialog,
        showEmailValidated,
        showMarketingPref,
        checkoutDialogTransaction,
        notifications,
        shouldResetBehavior,
        newBeneficiaryCreated,
        { viewModel.setShowEmailValidated(false) },
        { isAccepted -> viewModel.sendGDPRResponse(isAccepted) },
        callbacks,
        page,
        countryListType,
        originCountryList,
        destinationCountryList,
        recentCountriesUsed,
        object : HideDialogsCallbacks {
            override fun hideGdprDialog() {
                viewModel.hideGdprDialog()
            }

            override fun hideValidateEmailDialog() {
                viewModel.hideValidateEmailDialog()
            }

            override fun hideEmailValidatedDialog() {
                viewModel.hideEmailValidatedDialog()
            }

            override fun hideNewBeneficiaryDialog() {
                viewModel.showNewBeneficiaryCreatedDialog(false)
            }

            override fun hideMarketingPrefDialog() {
                viewModel.hideMarketingPrefDialog()
            }

            override fun hideCheckoutDialog() {
                viewModel.hideCheckoutDialog()
            }
        },
        { viewModel.setOriginCountry(it) },
        { viewModel.setDestinationCountry(it) },
        { viewModel.filterOrigin(it) },
        { viewModel.filterDestination(it) },
        showBottomSheet,
        { viewModel.hideBottomSheet() },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun Content(
    fragmentManager: FragmentManager,
    isLimitedUser: Boolean,
    destinationCountry: CountryUIModel,
    originCountry: CountryUIModel,
    gdpr: Gdpr,
    user: UserUIModel,
    sendEmailValidationDialog: String,
    showEmailValidated: Boolean,
    showMarketingPref: Boolean,
    transaction: CreateTransactionResponse,
    notifications: Int,
    shouldResetBehavior: Boolean,
    newBeneficiaryCreated: Boolean,
    setShowEmailValidated: Action,
    sendGDPRResponse: (Boolean) -> Unit,
    callbacks: HomeActivityCallbacks,
    pageToSet: Int,
    countryListType: CountryListType,
    originCountryList: List<SWCountryListWithSectionsItemUIModel>,
    destinationCountryList: List<SWCountryListWithSectionsItemUIModel>,
    recentCountriesUsed: List<SWCountryListWithSectionsItemUIModel>,
    hideDialogsCallbacks: HideDialogsCallbacks,
    originSelection: (country: CountryUIModel) -> Unit,
    destinationSelection: (country: CountryUIModel) -> Unit,
    onQueryOriginChanged: (query: String) -> Unit,
    onQueryDestinationChanged: (query: String) -> Unit,
    showBottomSheet: Boolean,
    hideBottomSheet: Action
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
    ) { 3 }

    val scope = rememberCoroutineScope()

    var pageClicked by rememberSaveable {
        mutableIntStateOf(pageToSet)
    }

    val lastScreenPosition by rememberSaveable { mutableIntStateOf(0) }

    val sendToFragment = SendToFragment().also {
        Bundle().apply {
            if (isLimitedUser) {
                putBoolean(SendToFragment.LIMITED_USER_KEY, true)
                putString(SendToFragment.ORIGIN_COUNTRY_ISO, originCountry.iso3)
                putString(SendToFragment.ORIGIN_COUNTRY, originCountry.name)
                putString(SendToFragment.DESTINATION_COUNTRY_ISO, destinationCountry.iso3)
                putString(SendToFragment.DESTINATION_COUNTRY, destinationCountry.name)
                it.arguments = this
            } else {
                putBoolean(SendToFragment.LIMITED_USER_KEY, false)
                it.arguments = this
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            when (page) {
                0 -> callbacks.registerEvent("click_menu_send_to", "", lastPageScreenName(lastScreenPosition))
                1 -> callbacks.registerEvent("click_menu_status", "", lastPageScreenName(lastScreenPosition))
                2 -> callbacks.registerEvent("click_menu_account", "", lastPageScreenName(lastScreenPosition))
                else -> {}
            }
            callbacks.trackScreen(currentPageScreenName(lastScreenPosition, isLimitedUser))
        }
    }

    LaunchedEffect(pageClicked) {
        scope.launch {
            pagerState.animateScrollToPage(page = pageClicked)
        }
    }

    LaunchedEffect(shouldResetBehavior) {
        if (shouldResetBehavior) sendToFragment.resetBehavior()
    }

    if (!gdpr.listGdprMessages.isNullOrEmpty()) {
        val isOld = gdpr.type?.equals("preferences") ?: false
        GdprDialog(
            onCancel = {
                if (!isOld) sendGDPRResponse(false)
                hideDialogsCallbacks.hideGdprDialog()
            },
            onAccept = {
                if (isOld) callbacks.navigateToMarketingPreferences(gdpr.title ?: STRING_EMPTY, Constants.GDPR_VALUES.FROM_VIEW_POPUP)
                else sendGDPRResponse(true)
                hideDialogsCallbacks.hideGdprDialog()
            },
            title = gdpr.title ?: STRING_EMPTY,
            messages = gdpr.listGdprMessages,
            isOld = isOld,
            positiveText = gdpr.buttonOkTitle ?: STRING_EMPTY,
            negativeText = gdpr.buttonCancelTitle ?: STRING_EMPTY,
            trackEvent = {
                callbacks.registerEvent(
                    ScreenCategory.TRANSFER.value,
                    "click_continue",
                    ScreenName.MODAL_MAX_AMOUNT.value,
                )
            },
        )
    }

    if (sendEmailValidationDialog.isNotEmpty())
        ValidateEmailDialog(
            onDismiss = { hideDialogsCallbacks.hideValidateEmailDialog() },
            email = sendEmailValidationDialog,
        )

    if (showEmailValidated)
        SWInfoDialog(
            title = stringResource(id = R.string.email_validation_title),
            content = stringResource(id = R.string.email_validation_text),
            positiveText = stringResource(id = R.string.cancel_transaction_transaction_status_button_close),
            positiveAction = {
                setShowEmailValidated()
                hideDialogsCallbacks.hideEmailValidatedDialog()
            },
            dismissAction = { hideDialogsCallbacks.hideEmailValidatedDialog() },
        )

    if (newBeneficiaryCreated) {
        SWInfoDialog(
            title = stringResource(id = R.string.action_done_transactional_calculator),
            content = stringResource(id = R.string.new_beneficiary_created_ok_text),
            positiveText = stringResource(id = R.string.accept_text),
            positiveAction = { hideDialogsCallbacks.hideNewBeneficiaryDialog() },
            dismissAction = { hideDialogsCallbacks.hideNewBeneficiaryDialog() },
        )
    }

    if (showMarketingPref)
        MarketingPreferencesDialog(
            onNavigateToMarketingPref = { callbacks.navigateToMarketingPreferences(STRING_EMPTY, Constants.GDPR_VALUES.FROM_VIEW_ACCOUNT) },
            onDismiss = { hideDialogsCallbacks.hideMarketingPrefDialog() },
        )

    if (transaction.transaction != null || transaction.errors != null) {
        var isTransactionSuccessful = true
        if (transaction.errors != null) {
            for (error: TransactionErrors in transaction.errors) {
                if (error.isBlocking) {
                    isTransactionSuccessful = false
                    break
                }
            }
        }
        CheckoutDialog(
            checkoutDialogUIModel = CheckoutDialogUIModel(
                isUserFromUSA = user.country.countries[0].iso3 == "USA",
                errors = transaction.errors.toMutableList(),
                style = if (isTransactionSuccessful) Constants.DIALOG_CHECKOUT_STYLE.SUCCESS_STYLE else Constants.DIALOG_CHECKOUT_STYLE.ERROR_STYLE,
                summary = transaction.summary,
                transaction = transaction.transaction,
                payNow = { mtn ->
                    hideDialogsCallbacks.hideCheckoutDialog()
                    callbacks.payNow(mtn)
                },
                showTransactionDetails = { trans ->
                    hideDialogsCallbacks.hideCheckoutDialog()
                    callbacks.showTransactionDetails(trans)
                },
                requestHelpEmail = { },
                closeDialog = {
                    hideDialogsCallbacks.hideCheckoutDialog()
                    callbacks.closeCheckoutDialog(isTransactionSuccessful)
                },
                registerEvent = { action, screen -> callbacks.registerEvent(action, STRING_EMPTY, screen) },
            ),
        )
    }

    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            coroutineScope.launch {
                sheetState.show()
            }
        }
    }

    if (!sheetState.isVisible) {
        hideBottomSheet()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            SWBottomSheetContentCountryList(
                sheetState = sheetState,
                countryListType = countryListType,
                elements = if (countryListType is CountryListType.SendMoneyFrom) originCountryList else recentCountriesUsed + destinationCountryList,
                dismissBottomSheetEvent = { eventAction, hierarchy ->
                    callbacks.registerEvent(eventAction, STRING_EMPTY, hierarchy)
                },
                selectedOriginCountry = originCountry,
                selectedDestinationCountry = destinationCountry,
                onQueryChanged = {
                    if (countryListType is CountryListType.SendMoneyFrom) {
                        onQueryOriginChanged(it)
                    } else {
                        onQueryDestinationChanged(it)
                    }
                },
                onOriginCountrySelected = {
                    originSelection(it)
                },
                onDestinationCountrySelected = {
                    destinationSelection(it)
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            TabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selectedTabIndex = pagerState.currentPage,
                divider = { },
                indicator = { tabPositions ->
                    if (pagerState.currentPage < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            color = colorBlueOceanDark,
                        )
                    }
                },
                tabs = {
                    PagerItem(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { pageClicked = 0 },
                        title = stringResource(id = R.string.tab_calculator_title),
                        textColor = if (pagerState.currentPage == 0) black else defaultTextColor,
                    )
                    PagerItem(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { pageClicked = 1 },
                        title = if (isLimitedUser) stringResource(id = R.string.tab_tracking_title) else stringResource(id = R.string.tab_status_title),
                        textColor = if (pagerState.currentPage == 1) black else defaultTextColor,
                    )
                    PagerItem(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { pageClicked = 2 },
                        title = stringResource(id = R.string.tab_account_title),
                        textColor = if (pagerState.currentPage == 2) black else defaultTextColor,
                        notificationNumber = notifications,
                    )
                },
            )

            val beneficiaryListListener = BeneficiaryListTouchListener

            val userScrollEnabled by remember { beneficiaryListListener.userScrollEnabled }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = userScrollEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f),
            ) { page ->
                when (page) {
                    0 -> {
                        FragmentContainer(
                            modifier = Modifier
                                .semantics { contentDescription = "sendTo" },
                            fragmentManager = fragmentManager,
                            commit = {
                                add(it, sendToFragment.apply { (this).setListener(beneficiaryListListener) })
                            },
                        )
                    }

                    1 -> {
                        FragmentContainer(
                            modifier = Modifier.semantics { contentDescription = "status" },
                            fragmentManager = fragmentManager,
                            commit = {
                                add(it, if (isLimitedUser) TrackingFragment() else StatusFragment())
                            },
                        )
                    }

                    2 -> {
                        FragmentContainer(
                            modifier = Modifier.semantics { contentDescription = "account" },
                            fragmentManager = fragmentManager,
                            commit = {
                                add(it, if (isLimitedUser) FreeUserFragment() else AccountFragment())
                            },
                        )
                    }
                }
            }

            if (isLimitedUser) {
                LoginToSendFooter(
                    modifier = Modifier
                        .fillMaxWidth(),
                    navigateToWelcomeActivity = callbacks.navigateToWelcomeActivity,
                )
            }
        }
    }
}

private fun lastPageScreenName(lastScreenPosition: Int): String {
    return when (lastScreenPosition) {
        0 -> ScreenName.SEND_TO_SCREEN.value
        1 -> ScreenName.STATUS_SCREEN.value
        2 -> ScreenName.ACCOUNT_SCREEN.value
        else -> STRING_EMPTY
    }
}

private fun currentPageScreenName(lastScreenPosition: Int, isLimitedUser: Boolean): String {
    return when (lastScreenPosition) {
        0 -> SendToFragment.SCREEN_NAME
        1 -> {
            if (isLimitedUser) {
                TrackingFragment.SCREEN_NAME
            } else {
                StatusFragment.SCREEN_NAME
            }
        }

        2 -> AccountFragment.SCREEN_NAME
        else -> STRING_EMPTY
    }
}

interface HideDialogsCallbacks {
    fun hideGdprDialog()
    fun hideValidateEmailDialog()
    fun hideEmailValidatedDialog()
    fun hideNewBeneficiaryDialog()
    fun hideMarketingPrefDialog()
    fun hideCheckoutDialog()
}

object PreviewFragmentManager : FragmentManager()

@Preview(showBackground = true, widthDp = 420)
@Composable
fun HomeActivityLayoutPreview() {
    Content(
        PreviewFragmentManager,
        false,
        CountryUIModel(),
        CountryUIModel(),
        Gdpr(),
        UserUIModel(),
        STRING_EMPTY,
        false,
        false,
        CreateTransactionResponse(),
        12,
        false,
        false,
        { },
        { _ -> },
        HomeActivityCallbacks(
            { _, _, _ -> },
            { _ -> },
            {},
            { _, _ -> },
            { _ -> },
            { _ -> },
            {},
            {},
            {},
        ),
        0,
        CountryListType.SendMoneyFrom,
        emptyList(),
        emptyList(),
        emptyList(),
        object : HideDialogsCallbacks {
            override fun hideGdprDialog() {}
            override fun hideValidateEmailDialog() {}
            override fun hideEmailValidatedDialog() {}
            override fun hideNewBeneficiaryDialog() {}
            override fun hideMarketingPrefDialog() {}
            override fun hideCheckoutDialog() {}
        },
        {},
        {},
        {},
        {},
        false,
        {},
    )
}
