package com.smallworldfs.moneytransferapp.compose.widgets.transferdetails

import android.text.TextUtils
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTextButton
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.SecondaryAction
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel

@Composable
fun SWTransferSummaryItemFooter(
    transaction: TransactionUIModel,
    buttonAction: (TransactionUIModel) -> Unit
) {
    with(transaction) {
        val buttonActionText = if (offline) stringResource(id = R.string.offline_transaction_action_label) else transaction.secondaryAction.buttonText
        val buttonActionColor = if (offline) darkGreyText else blueAccentColor
        val buttonActionCallback: (TransactionUIModel) -> Unit = if (offline) { _ -> Unit } else { _ -> buttonAction(transaction) }

        if (!TextUtils.isEmpty(secondaryAction.buttonText) && !transaction.boleto) {
            SWTextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                colorText = buttonActionColor,
                text = buttonActionText,
                icon = secondaryAction.icon,
                iconColor = buttonActionColor,
                clickAction = { buttonActionCallback(transaction) },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTransferSummaryItemFooterPreview() {
    SWTransferSummaryItemFooter(
        transaction = TransactionUIModel(
            offline = false,
            boleto = false,
            secondaryAction = SecondaryAction(buttonText = "Button Action", icon = R.drawable.ic_action_close_white),

        ),
        buttonAction = {}
    )
}
