package com.smallworldfs.moneytransferapp.compose.widgets.transferdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.smallworldfs.moneytransferapp.compose.colors.mainBlue
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import java.util.Calendar

@Composable
fun SWTransferBeneficiaryInfo(
    transaction: TransactionUIModel,
    onCardClickCallback: Action,
    showArrowIcon: Boolean
) {

    ConstraintLayout {
        val (blueBox, infoCards) = createRefs()
        Box(
            modifier = Modifier
                .background(mainBlue)
                .fillMaxWidth()
                .size(60.dp)
                .constrainAs(blueBox) {}
        )
        Column(
            modifier = Modifier
                .constrainAs(infoCards) {
                    top.linkTo(blueBox.top, 12.dp)
                }
        ) {
            Card(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                SWTransferSummaryItemBody(
                    transaction = transaction,
                    onCardClickCallback = { onCardClickCallback() },
                    showArrowIcon = showArrowIcon
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTransferBeneficiaryInfoPreview() {
    SWTransferBeneficiaryInfo(
        transaction = TransactionUIModel(
            offline = true,
            beneficiaryFirstName = "Test",
            beneficiaryLastName = "Test",
            beneficiaryCountry = "ARG",
            payoutPrincipal = "22355",
            payoutCurrency = "ARS",
            translatedDeliveryMethod = "PHYSICAL DELIVERY",
            createdAt = Calendar.getInstance(),
        ),
        onCardClickCallback = {},
        showArrowIcon = false
    )
}
