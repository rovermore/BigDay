package com.smallworldfs.moneytransferapp.compose.widgets.transferdetails

import android.text.TextUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyControl
import com.smallworldfs.moneytransferapp.compose.colors.greySeparator
import com.smallworldfs.moneytransferapp.compose.widgets.SWImageFlag
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.ComplianceDocDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.AmountFormatter
import com.smallworldfs.moneytransferapp.utils.Constants
import java.lang.Double
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.Boolean
import kotlin.String
import kotlin.with

@Composable
fun SWTransferSummaryItemBody(
    modifier: Modifier = Modifier,
    transaction: TransactionUIModel,
    onCardClickCallback: Action,
    showArrowIcon: Boolean
) {

    with(transaction) {
        val day = createdAt.get(Calendar.DAY_OF_MONTH).toString()
        val month = SimpleDateFormat("MMM", Locale.getDefault()).format(createdAt.time)

        val beneficiaryFirstLetter = if (!TextUtils.isEmpty(beneficiaryFirstName)) beneficiaryFirstName.substring(0, 1).uppercase(Locale.getDefault()) else ""
        val beneficiarySecondLetter = if (!TextUtils.isEmpty(beneficiaryLastName)) beneficiaryLastName.substring(0, 1).uppercase(Locale.getDefault()) else ""

        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onCardClickCallback() },
        ) {
            Beneficiary(
                day = day,
                month = month,
                beneficiaryCountry = beneficiaryCountry,
                beneficiaryFirstLetter = beneficiaryFirstLetter,
                beneficiarySecondLetter = beneficiarySecondLetter,
            )

            Divider(
                color = greySeparator,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(1.dp)
                    .height(64.dp),
            )

            Transfer(
                transaction = transaction,
                showArrowIcon = showArrowIcon,
            )
        }
    }
}

@Composable
fun Beneficiary(
    day: String,
    month: String,
    beneficiaryCountry: String,
    beneficiaryFirstLetter: String,
    beneficiarySecondLetter: String
) {
    Row {
        ConstraintLayout {
            val (beneficiaryCircle, flagCircle) = createRefs()

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .background(defaultGreyControl, shape = CircleShape)
                    .constrainAs(beneficiaryCircle) {},
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    SWText(
                        text = beneficiaryFirstLetter,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        color = darkGreyText,
                    )
                    SWText(
                        modifier = Modifier
                            .padding(bottom = 4.dp),
                        text = beneficiarySecondLetter,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = darkGreyText,
                    )
                }
            }

            SWImageFlag(
                modifier = Modifier
                    .size(24.dp)
                    .background(defaultGreyControl, shape = CircleShape)
                    .constrainAs(flagCircle) {
                        bottom.linkTo(beneficiaryCircle.bottom, 12.dp)
                        end.linkTo(beneficiaryCircle.end, 12.dp)
                    },
                imageUrl = Constants.COUNTRY.FLAG_IMAGE_ASSETS + beneficiaryCountry + Constants.COUNTRY.FLAG_IMAGE_EXTENSION,
            )
        }

        Column(
            modifier = Modifier
                .padding(
                    top = 20.dp,
                    end = 16.dp,
                ),
            verticalArrangement = Arrangement.spacedBy((-8).dp),
        ) {
            SWText(
                text = day,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
            )

            SWText(
                text = month,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
fun Transfer(
    transaction: TransactionUIModel,
    showArrowIcon: Boolean
) {

    with(transaction) {
        val isOfflineTransaction =
            offline && (
                payoutPrincipal.isEmpty() ||
                    beneficiaryFirstName.isEmpty() ||
                    beneficiaryLastName.isEmpty() ||
                    payoutCurrency.isEmpty() ||
                    translatedDeliveryMethod.isEmpty()
                )

        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            if (isOfflineTransaction) {
                Column {
                    SWText(
                        text = "MTN: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                    SWText(
                        text = ComplianceDocDTO.Other.mtn,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                }
            } else {
                Column {
                    SWText(
                        text = "$beneficiaryFirstName $beneficiaryLastName",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                    )
                    SWText(
                        text = AmountFormatter.formatDoubleAmountNumber(Double.valueOf(payoutPrincipal)) + " " + payoutCurrency,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                    SWText(
                        text = deliveryMethod,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (showArrowIcon) {
                Icon(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    painter = painterResource(id = R.drawable.account_icn_arrowactivitycard),
                    contentDescription = "logo",
                    tint = blueAccentColor,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTransferSummaryItemBodyPreview() {
    SWTransferSummaryItemBody(
        transaction = TransactionUIModel(
            offline = false,
            mtn = "32176781",
            beneficiaryCountry = "ARG",
            beneficiaryFirstName = "First name",
            beneficiaryLastName = "Last name",
            payoutPrincipal = "22345",
            payoutCurrency = "ARS",
            deliveryMethod = "BANK_TRANSFER",
        ),
        onCardClickCallback = {},
        showArrowIcon = true,
    )
}
