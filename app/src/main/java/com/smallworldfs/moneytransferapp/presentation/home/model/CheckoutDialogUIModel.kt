package com.smallworldfs.moneytransferapp.presentation.home.model

import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionCheckOutDialogSummary
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors

data class CheckoutDialogUIModel(
    val isUserFromUSA: Boolean = false,
    val errors: MutableList<TransactionErrors>,
    val style: String,
    val summary: TransactionCheckOutDialogSummary,
    val transaction: Transaction?,
    val payNow: (String) -> Unit,
    val showTransactionDetails: (Transaction) -> Unit,
    val requestHelpEmail: Action,
    val closeDialog: Action,
    val registerEvent: (String, String) -> Unit
)
