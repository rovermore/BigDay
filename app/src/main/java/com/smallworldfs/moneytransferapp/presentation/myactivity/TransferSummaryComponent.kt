package com.smallworldfs.moneytransferapp.presentation.myactivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.mediumBlue
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.transferdetails.SWTransferSummaryItemBody
import com.smallworldfs.moneytransferapp.presentation.myactivity.model.MonthlyTransactionHistoryDetail
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.STRING_SPACE

@Composable
fun TransactionHistoryBarSummary(selectedTransaction: MonthlyTransactionHistoryDetail, monthlyTransactions: ArrayList<TransactionUIModel>?, onActionShowTransactionDetail: (TransactionUIModel) -> Unit) {
    Column(Modifier.background(defaultGreyLightBackground)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(neutral0),
        ) {
            SWText(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                textAlign = TextAlign.Center, text = selectedTransaction.monthName, fontSize = 18.sp, color = mediumBlue,
            )

            Divider(Modifier.padding(vertical = 10.dp, horizontal = 10.dp))

            TitleAndValue(stringResource(id = R.string.beneficiary_detail_activity_total_paid), String.format("%.2f", selectedTransaction.monthlyTotal) + STRING_SPACE + selectedTransaction.sendingCurrency)

            TitleAndValue(stringResource(id = R.string.beneficiary_detail_activity_transfers_made), selectedTransaction.transactionsCount.toString())
        }

        if (!monthlyTransactions.isNullOrEmpty()) {
            monthlyTransactions.forEach { transaction ->
                SWTransferSummaryItemBody(
                    Modifier
                        .padding(vertical = 5.dp)
                        .background(neutral0)
                        .clip(RoundedCornerShape(10.dp)),
                    transaction = transaction,
                    onCardClickCallback = { onActionShowTransactionDetail(transaction) },
                    showArrowIcon = true,
                )
            }
        }
    }
}

@Composable
fun TitleAndValue(heading: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        SWText(text = heading, color = darkGreyText, fontSize = 16.sp)

        SWText(text = value, color = darkGreyText, fontSize = 20.sp)
    }
}

@Preview
@Composable
fun TitleAndValuePreview() {
    TitleAndValue(heading = "Total Paid", value = "20.00")
}

@Preview
@Composable
fun TransactionHistoryBarSummaryPreview() {
    TransactionHistoryBarSummary(MonthlyTransactionHistoryDetail(month = "Jan", 250.00, 5, "January", "USD"), monthlyTransactions = ArrayList<TransactionUIModel>(), onActionShowTransactionDetail = {})
}
