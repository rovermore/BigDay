package com.smallworldfs.moneytransferapp.compose.widgets.transferdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.widgets.SWAccountMenuItem
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuItemUIModel
import com.smallworldfs.moneytransferapp.presentation.account.account.model.MenuIcon
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.Constants

@Composable
fun SWTransferHelpButtons(
    transaction: TransactionUIModel,
    listener: SWTransferHelpButtonsListener
) {
    Card {
        Column {
            SWAccountMenuItem(
                modifier = Modifier
                    .semantics {
                        contentDescription = "customer_support_button"
                    },
                item = AccountMenuItemUIModel(
                    description = stringResource(id = R.string.contact_customer_support_transaction_status_button),
                    icon = MenuIcon.Icon(resourceId = R.drawable.status_icn_help),
                ),
                onItemClicked = { listener.onContactSupport() },
            )

            Divider(thickness = 1.dp)

            if (transaction.senderCountry == Constants.COUNTRY.US_COUNTRY_VALUE) {
                SWAccountMenuItem(
                    item = AccountMenuItemUIModel(
                        description = stringResource(id = R.string.transaction_status_detail_activity_view_pre_payment_disclosure),
                        icon = MenuIcon.Icon(resourceId = R.drawable.status_icn_receipt),
                    ),
                    onItemClicked = { listener.onShowPreReceipt() },
                )

                Divider(thickness = 1.dp)

                if (transaction.paid) {
                    SWAccountMenuItem(
                        modifier = Modifier
                            .semantics {
                                contentDescription = "show_receipt_button"
                            },
                        item = AccountMenuItemUIModel(
                            description = stringResource(id = R.string.show_receipt_transaction_status_button),
                            icon = MenuIcon.Icon(resourceId = R.drawable.status_icn_receipt),
                        ),
                        onItemClicked = { listener.onShowReceipt() },
                    )

                    Divider(thickness = 1.dp)
                }
            } else {
                SWAccountMenuItem(
                    modifier = Modifier
                        .semantics {
                            contentDescription = "show_receipt_button"
                        },
                    item = AccountMenuItemUIModel(
                        description = if (transaction.offline) stringResource(id = R.string.no_valid_receipt) else stringResource(id = R.string.show_receipt_transaction_status_button),
                        icon = MenuIcon.Icon(resourceId = R.drawable.status_icn_receipt),
                    ),
                    onItemClicked = { listener.onShowReceipt() },
                )

                Divider(thickness = 1.dp)
            }

            if (transaction.cancellable) {
                SWAccountMenuItem(
                    item = AccountMenuItemUIModel(
                        description = stringResource(id = R.string.cancel_transaction_transaction_status_button),
                        icon = MenuIcon.Icon(resourceId = R.drawable.status_icn_cancel),
                    ),
                    onItemClicked = { listener.onCancelTransaction() },
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTransferHelpButtonsPreview() {
    SWTransferHelpButtons(
        transaction = TransactionUIModel(),
        listener = object : SWTransferHelpButtonsListener {
            override fun onContactSupport() = Unit
            override fun onShowPreReceipt() = Unit
            override fun onShowReceipt() = Unit
            override fun onCancelTransaction() = Unit
        },
    )
}

interface SWTransferHelpButtonsListener {
    fun onContactSupport()
    fun onShowPreReceipt()
    fun onShowReceipt()
    fun onCancelTransaction()
}
