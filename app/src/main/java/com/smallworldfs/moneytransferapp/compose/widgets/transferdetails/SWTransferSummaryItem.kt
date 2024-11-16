package com.smallworldfs.moneytransferapp.compose.widgets.transferdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.SecondaryAction
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.Constants

@Composable
fun SWTransferSummaryItem(
    modifier: Modifier = Modifier,
    transaction: TransactionUIModel,
    onCardClickCallback: (TransactionUIModel) -> Unit,
    onActionButtonCallback: (TransactionUIModel) -> Unit
) {
    Card(modifier) {
        Column {
            SWTransferSummaryItemHeader(
                transactionStatus = transaction.statusText,
                showBubbleIndicator = transaction.isNewTransaction(),
                transactionIcon = transaction.statusIcon,
            )

            Divider(thickness = 1.dp)

            SWTransferSummaryItemBody(
                transaction = transaction,
                onCardClickCallback = { onCardClickCallback(transaction) },
                showArrowIcon = true,
            )

            Divider(thickness = 1.dp)

            SWTransferSummaryItemFooter(
                transaction = transaction,
                buttonAction = { onActionButtonCallback(transaction) },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTransferSummaryItemPreview() {
    SWTransferSummaryItem(
        transaction = TransactionUIModel(
            offline = false,
            status = Constants.TRANSACTION_STATUS.NEW,
            statusText = "Bank Pending",
            statusIcon = R.drawable.freshchat_cross_icon,
            translatedDeliveryMethod = "Cash Pickup",
            secondaryAction = SecondaryAction(buttonText = "Button Action", icon = R.drawable.ic_action_close_white),
            beneficiaryFirstName = "Test",
            beneficiaryLastName = "Test",
            beneficiaryCountry = "ARG",
            payoutPrincipal = "22354",
            payoutCurrency = "ARS",
            mtn = "28746238743",
            boleto = false,
        ),
        onActionButtonCallback = { _ -> },
        onCardClickCallback = {},
    )
}
