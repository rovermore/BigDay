package com.smallworldfs.moneytransferapp.presentation.transferdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyControl
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.dialogs.SWWarningDialog
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun TransferDetailsLayout(
    transferData: Transaction,
    onClipboardClick: (String) -> Unit,
    onBackPressed: Action,
    viewModel: TransferDetailViewModel = viewModel()
) {

    viewModel.getTransferDetails(transferData)

    val transactionDetails by viewModel.transferDetail.collectAsStateWithLifecycle()
    val error by viewModel.transferDetailError.collectAsStateWithLifecycle()

    Content(
        transferDetail = transactionDetails.details,
        country = transactionDetails.country,
        error = error,
        onClipboardClick = { data -> onClipboardClick(data) },
        onDialogClick = { viewModel.hideErrorDialog() },
        onBackPressed = { onBackPressed() }
    )
}

@Composable
fun Content(
    transferDetail: Transaction,
    country: String,
    error: ErrorType,
    onClipboardClick: (String) -> Unit,
    onDialogClick: Action,
    onBackPressed: Action
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = defaultGreyControl)
    ) {

        SWTopAppBar(
            barTitle = stringResource(id = R.string.transfer_details_title),
            onBackPressed = { onBackPressed() },
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {

            if (error !is ErrorType.None) {
                SWWarningDialog(
                    title = stringResource(id = R.string.generic_title_error),
                    content = stringResource(id = R.string.generic_error_view_text),
                    positiveText = stringResource(id = R.string.accept_text),
                    positiveAction = { onDialogClick() },
                    dismissAction = { onDialogClick() },
                )
            }

            SWText(
                text = stringResource(id = R.string.transfer_details_header),
                fontWeight = FontWeight.SemiBold,
                color = darkGreyText
            )

            Card(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                backgroundColor = defaultGreyLightBackground
            ) {

                Column {

                    TransferDetailItem(
                        title = stringResource(id = R.string.transfer_detail_activity_transfer_to),
                        description = transferDetail.bankTitular,
                        iconAction = { onClipboardClick(transferDetail.bankTitular) }
                    )

                    TransferDetailItem(
                        title = stringResource(id = R.string.transfer_detail_activity_total_amount),
                        description = transferDetail.totalSale,
                        iconAction = { onClipboardClick(transferDetail.totalSale) }
                    )

                    TransferDetailItem(
                        title = transferDetail.bankName,
                        description = getBankData(transferDetail, country),
                        iconAction = { onClipboardClick(getBankData(transferDetail, country)) }
                    )

                    TransferDetailItem(
                        title = stringResource(id = R.string.transfer_detail_activity_reference),
                        description = transferDetail.mtn,
                        iconAction = { onClipboardClick(transferDetail.mtn) }
                    )
                }
            }

            SWText(
                text = stringResource(id = R.string.transfer_details_footer),
                fontWeight = FontWeight.SemiBold,
                color = darkGreyText
            )
        }
    }
}

@Composable
fun TransferDetailItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    iconAction: Action
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {

            SWText(
                text = title,
                fontWeight = FontWeight.Bold,
                color = darkGreyText
            )

            SWText(
                modifier = Modifier
                    .padding(top = 16.dp),
                text = description,
                fontWeight = FontWeight.SemiBold,
                color = darkGreyText
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier
                .width(36.dp)
                .height(36.dp)
                .clickable { iconAction() }
                .semantics { contentDescription = "copy" },
            painter = painterResource(id = R.drawable.details_icn_copy),
            contentDescription = "clipboard",
            tint = blueAccentColor,
        )
    }

    Divider(
        thickness = 1.dp
    )
}

private fun getBankData(transaction: Transaction, country: String): String {
    return if (country == "GBR") {
        val gbAccountNumberList = transaction.bankNumber.split(
            "Sort",
            ignoreCase = false,
            limit = 2,
        )
        val bankData = if (gbAccountNumberList.size == 2) {
            "${gbAccountNumberList[0]}\nSort${gbAccountNumberList[1]}"
        } else {
            gbAccountNumberList[0]
        }
        bankData
    } else {
        transaction.bankIban ?: STRING_EMPTY
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun TransferDetailsLayoutPreview() {
    Content(
        transferDetail = Transaction(),
        country = STRING_EMPTY,
        error = ErrorType.None,
        onClipboardClick = {},
        onDialogClick = {},
        onBackPressed = {}
    )
}
