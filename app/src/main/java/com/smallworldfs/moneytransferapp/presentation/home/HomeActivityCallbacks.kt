package com.smallworldfs.moneytransferapp.presentation.home

import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction

class HomeActivityCallbacks(
    val registerEvent: (String, String, String) -> Unit,
    val trackScreen: (String) -> Unit,
    val navigateToWelcomeActivity: Action,
    val navigateToMarketingPreferences: (String, String) -> Unit,
    val payNow: (String) -> Unit,
    val showTransactionDetails: (Transaction) -> Unit,
    val closeCheckoutDialog: (Boolean) -> Unit,
    val launchInAppReview: Action,
    val registerBrazeEvent: Action
)
