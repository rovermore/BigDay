package com.smallworldfs.moneytransferapp.compose.widgets.transferdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTextButton
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.SecondaryAction
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWTransferStatusInfo(
    transaction: TransactionUIModel,
    listener: SWTransferStatusInfoListener
) {

    with(transaction) {
        val shouldShowTwoButtons = !isChallenge && isPaymentMethodChangeable()
        val leftButtonText = secondaryAction.buttonText
        var rightButtonText = STRING_EMPTY
        val iconButton = secondaryAction.icon

        if (isPaymentMethodChangeable()) {
            rightButtonText = stringResource(id = R.string.bank_transfer)
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 4.dp)
                        .wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    statusIcon?.let {
                        Icon(
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp),
                            painter = painterResource(id = it),
                            contentDescription = "logo",
                            tint = black,
                        )
                    }

                    SWText(
                        modifier = Modifier
                            .padding(start = 4.dp),
                        text = statusText,
                        color = black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                if (statusMsg.isNotEmpty()) {
                    SWText(
                        modifier = Modifier
                            .padding(start = 12.dp, bottom = 8.dp),
                        text = statusMsg,
                        color = black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Divider(thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (leftButtonText.isNotEmpty()) {
                        if (shouldShowTwoButtons) {
                            SWTextButton(
                                colorText = blueAccentColor,
                                text = leftButtonText,
                                icon = secondaryAction.icon,
                                iconColor = blueAccentColor,
                                fontSize = 14.sp,
                                clickAction = { listener.onLeftButton() },
                            )

                            SWTextButton(
                                modifier = Modifier
                                    .weight(1f),
                                colorText = colorGrayLight,
                                text = stringResource(id = R.string.or_by_text_separator),
                                fontSize = 16.sp,
                                clickAction = { },
                            )

                            SWTextButton(
                                colorText = blueAccentColor,
                                text = rightButtonText,
                                icon = secondaryAction.icon,
                                iconColor = blueAccentColor,
                                fontSize = 14.sp,
                                clickAction = { listener.onRightButton() },
                            )
                        } else {
                            SWTextButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colorText = blueAccentColor,
                                text = leftButtonText,
                                icon = iconButton,
                                iconColor = blueAccentColor,
                                clickAction = {
                                    if (cancellable) {
                                        listener.onCancelButton()
                                    } else if (secondaryAction.type == SecondaryAction.Type.AWAITING_BANK_TRANSFER) {
                                        listener.onShowDetails(transaction)
                                    } else {
                                        listener.onPayNowClick()
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

interface SWTransferStatusInfoListener {
    fun onCancelButton()
    fun onShowDetails(transaction: TransactionUIModel)
    fun onPayNowClick()
    fun onLeftButton()
    fun onRightButton()
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTransferStatusInfoPreview() {
    SWTransferStatusInfo(
        transaction = TransactionUIModel(
            statusIcon = R.drawable.ic_action_close_white,
            statusText = "Pending Bank Transfer",
            statusMsg = "Pending payment",
            secondaryAction = SecondaryAction(buttonText = "BUTTON ACTION", icon = R.drawable.ic_action_close_white),
            isChallenge = false,
            paymentType = Constants.PAYNMENT_METHODS.ONLINEPAYMENT,
            cancellable = false,
        ),
        listener = object : SWTransferStatusInfoListener {
            override fun onCancelButton() = Unit
            override fun onShowDetails(transaction: TransactionUIModel) = Unit
            override fun onPayNowClick() = Unit
            override fun onLeftButton() = Unit
            override fun onRightButton() = Unit
        },
    )
}
