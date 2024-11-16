package com.smallworldfs.moneytransferapp.presentation.home.dialog

import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.colorRedError
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.defaultTextColor
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.fonts.fonts
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWImageFlag
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionCheckOutDialogSummary
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors
import com.smallworldfs.moneytransferapp.presentation.home.model.CheckoutDialogUIModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.util.Locale

@Composable
fun CheckoutDialog(
    checkoutDialogUIModel: CheckoutDialogUIModel
) {
    val isTransactionSuccessful = checkoutDialogUIModel.style != Constants.DIALOG_CHECKOUT_STYLE.ERROR_STYLE

    if (checkoutDialogUIModel.transaction != null && checkoutDialogUIModel.transaction.mtn.isNotEmpty()) {
        checkoutDialogUIModel.errors.add(
            0,
            TransactionErrors(
                if (checkoutDialogUIModel.isUserFromUSA) stringResource(id = R.string.order_placed_successfully_usa)
                else stringResource(id = R.string.order_placed_successfully),
                stringResource(id = R.string.your_mtn_is).plus(checkoutDialogUIModel.transaction.mtn),
            ),
        )
    }

    val buttonText: String? =
        if (checkoutDialogUIModel.transaction?.isChallenge == true) {
            STRING_EMPTY
        } else {
            checkoutDialogUIModel.transaction?.let {
                if (checkoutDialogUIModel.transaction.isCanCanceled) {
                    stringResource(R.string.cancel_transaction_transaction_status_button).uppercase(Locale.getDefault())
                } else {
                    if (checkoutDialogUIModel.transaction.isBoleto) {
                        stringResource(R.string.secondary_button_text_action_print_boleto)
                    } else {
                        when (getSecondaryActionType(it)) {
                            Transaction.SecondaryAction.AWAITING_BANK_TRANSFER ->
                                stringResource(R.string.secondary_button_text_action_transfer_details_dialog)

                            Transaction.SecondaryAction.AWAITING_PAYMENT ->
                                if (checkoutDialogUIModel.transaction.isTransactionPaid) {
                                    null
                                } else {
                                    stringResource(R.string.secondary_button_text_action_pay_now)
                                }

                            else -> null
                        }
                    }
                }
            }
        }

    Dialog(
        onDismissRequest = { },
    ) {
        ConstraintLayout {
            val (icon, layout, flag) = createRefs()

            if (!isTransactionSuccessful) {
                Image(
                    painter = painterResource(R.drawable.checkout_icn_blockingalert),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(colorRedError, CircleShape)
                        .border(4.dp, neutral0, CircleShape)
                        .constrainAs(icon) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .zIndex(1f),
                )
            } else {
                SWImageFlag(
                    imageUrl = Constants.COUNTRY.FLAG_IMAGE_ASSETS + checkoutDialogUIModel.transaction?.beneficiaryCountry + Constants.COUNTRY.FLAG_IMAGE_EXTENSION,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(colorBlueOcean, CircleShape)
                        .border(4.dp, neutral0, CircleShape)
                        .constrainAs(flag) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .zIndex(1f),
                )
                Image(
                    painter = painterResource(R.drawable.ic_done),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .constrainAs(icon) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .zIndex(1f),
                )
            }
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(neutral0)
                    .fillMaxWidth()
                    .constrainAs(layout) {
                        top.linkTo(icon.top, 32.dp)
                    }
                    .clip(RoundedCornerShape(12.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd,
                ) {
                    Image(
                        painter = painterResource(R.drawable.checkout_icn_close),
                        contentDescription = "exit",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .size(44.dp)
                            .padding(top = 16.dp, end = 16.dp)
                            .clickable {
                                if (isTransactionSuccessful)
                                    checkoutDialogUIModel.registerEvent("click_close", ScreenName.ORDER_PLACED_SUCCESSFULLY.value)
                                else
                                    checkoutDialogUIModel.registerEvent("click_close", ScreenName.TRANSACTION_ORDER_FAILED.value)
                                checkoutDialogUIModel.closeDialog()
                            },
                    )
                }

                SWText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = if (isTransactionSuccessful) stringResource(id = R.string.checkout_popup_success_title)
                    else stringResource(id = R.string.checkout_popup_error_title),
                    color = black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                )

                val subtext: String =
                    if (isTransactionSuccessful) {
                        if (checkoutDialogUIModel.isUserFromUSA)
                            stringResource(id = R.string.checkout_popup_success_subtitle_usa)
                        else
                            stringResource(id = R.string.checkout_popup_success_subtitle)
                    } else stringResource(id = R.string.checkout_popup_error_subtitle)

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = subtext,
                    color = colorGrayLight,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    fontFamily = fonts,
                    textAlign = TextAlign.Center,
                )

                Image(
                    painter = painterResource(id = R.drawable.checkout_img_popupmask),
                    contentDescription = "separator",
                )

                if (checkoutDialogUIModel.errors.isNotEmpty())
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(defaultGreyLightBackground),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        content = {
                            items(checkoutDialogUIModel.errors) { error ->
                                var subtitle = STRING_EMPTY
                                var icon = R.drawable.checkout_icn_alert

                                if (!error.title.equals(error.description))
                                    subtitle = error.description

                                if (isTransactionSuccessful)
                                    icon = R.drawable.checkout_icn_check

                                CheckoutError(
                                    title = error.title,
                                    subtitle = subtitle,
                                    icon = icon,
                                )
                            }
                        },
                    )

                // If the user country is USA show the summary
                if (checkoutDialogUIModel.isUserFromUSA) Summary(checkoutDialogUIModel.summary)

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp,
                )

                SWButton(
                    modifier = Modifier
                        .padding(16.dp),
                    shape = RoundedCornerShape(50),
                    onClick = {
                        if (isTransactionSuccessful) {
                            checkoutDialogUIModel.transaction?.let {
                                if (getSecondaryActionType(checkoutDialogUIModel.transaction) == Transaction.SecondaryAction.AWAITING_PAYMENT) {
                                    checkoutDialogUIModel.registerEvent("click_pay_now", ScreenName.ORDER_PLACED_SUCCESSFULLY.value)
                                    checkoutDialogUIModel.payNow(checkoutDialogUIModel.transaction.mtn)
                                } else checkoutDialogUIModel.showTransactionDetails(checkoutDialogUIModel.transaction)
                            }
                        } else {
                            checkoutDialogUIModel.registerEvent("click_get_help", ScreenName.TRANSACTION_ORDER_FAILED.value)
                            checkoutDialogUIModel.requestHelpEmail()
                        }
                    },
                    text = if (isTransactionSuccessful)
                        buttonText ?: stringResource(id = R.string.get_help_checkout_button_popup)
                    else
                        stringResource(id = R.string.get_help_checkout_button_popup),
                    backgroundColor = colorGreenMain,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textModifier = Modifier.padding(0.dp),
                    textColor = colorBlueOcean,
                )
            }
        }
    }
}

@Composable
private fun Summary(
    summary: TransactionCheckOutDialogSummary
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(defaultGreyLightBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SWText(
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(start = 12.dp, end = 12.dp),
            text = stringResource(id = R.string.fragment_checkout_error_dialog_summary_information_title),
            fontWeight = FontWeight.Bold,
            color = black,
        )
        if (summary.sender.amount.label.isNotEmpty() && summary.sender.amount.value.isNotEmpty())
            SummaryRow(
                name = summary.sender.amount.label,
                value = summary.sender.amount.value,
            )
        if (summary.sender.fee.label.isNotEmpty() && summary.sender.fee.value.isNotEmpty())
            SummaryRow(
                name = summary.sender.fee.label,
                value = summary.sender.fee.value,
            )
        if (summary.sender.taxes.label.isNotEmpty() && summary.sender.taxes.value.isNotEmpty())
            SummaryRow(
                name = summary.sender.taxes.label,
                value = summary.sender.taxes.value,
            )
        if (summary.sender.total.label.isNotEmpty() && summary.sender.total.value.isNotEmpty())
            SummaryRow(
                name = summary.sender.total.label,
                value = summary.sender.total.value,
                isTotal = true,
            )
        if (summary.exchange_rate.label.isNotEmpty() && summary.exchange_rate.value.isNotEmpty())
            SummaryRow(
                name = summary.exchange_rate.label,
                value = summary.exchange_rate.value,
            )
        if (summary.receipt.amount.label.isNotEmpty() && summary.receipt.amount.value.isNotEmpty())
            SummaryRow(
                name = summary.receipt.amount.label,
                value = summary.receipt.amount.value,
            )
        if (summary.receipt.fee.label.isNotEmpty() && summary.receipt.fee.value.isNotEmpty())
            SummaryRow(
                name = summary.receipt.fee.label,
                value = summary.receipt.fee.value,
            )
        if (summary.receipt.total.label.isNotEmpty() && summary.receipt.total.value.isNotEmpty())
            SummaryRow(
                name = summary.receipt.total.label,
                value = summary.receipt.total.value,
                isTotal = true,
            )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            thickness = 1.dp,
        )
        if (summary.footer.isNotEmpty())
            SWText(
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .padding(start = 12.dp, end = 12.dp),
                text = summary.footer,
            )
    }
}

@Composable
private fun SummaryRow(
    name: String = STRING_EMPTY,
    value: String = STRING_EMPTY,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp),
    ) {
        SWText(
            modifier = Modifier.weight(1f),
            text = name,
            textAlign = TextAlign.Start,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Light,
            color = if (isTotal) black else defaultTextColor,
        )

        SWText(
            modifier = Modifier.weight(1f),
            text = value,
            textAlign = TextAlign.End,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Light,
            color = if (isTotal) black else defaultTextColor,
        )
    }
}

@Composable
private fun CheckoutError(
    title: String,
    subtitle: String,
    icon: Int = R.drawable.checkout_icn_alert
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.padding(12.dp),
            painter = painterResource(id = icon),
            contentDescription = "error icon",
        )
        Column {
            SWText(
                text = title,
                fontStyle = FontStyle.Italic,
                color = black,
            )
            SWText(
                text = subtitle,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

private fun getSecondaryActionType(transaction: Transaction): Transaction.SecondaryAction {
    if (TextUtils.isEmpty(transaction.status)) {
        return Transaction.SecondaryAction.EMPTY
    }
    return if (transaction.status.equals(Constants.TRANSACTION_STATUS.ACKNOWLEDGE_PENDING, ignoreCase = true) ||
        transaction.status.equals(Constants.TRANSACTION_STATUS.NEW, ignoreCase = true)
    ) {
        if (transaction.isTransactionPaid) {
            Transaction.SecondaryAction.EMPTY
        } else {
            if (transaction.paymentType.equals(Constants.PAYNMENT_METHODS.BANKWIRE, ignoreCase = true)) {
                Transaction.SecondaryAction.AWAITING_BANK_TRANSFER
            } else {
                Transaction.SecondaryAction.AWAITING_PAYMENT
            }
        }
    } else {
        Transaction.SecondaryAction.EMPTY
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun CheckoutDialogPreview() {
    CheckoutDialog(
        CheckoutDialogUIModel(
            false,
            mutableListOf(
                TransactionErrors("title", "description"),
                TransactionErrors("title", "description"),
                TransactionErrors("title", "description"),
                TransactionErrors("title", "description"),
                TransactionErrors("title", "description"),
            ),
            Constants.DIALOG_CHECKOUT_STYLE.SUCCESS_STYLE,
            TransactionCheckOutDialogSummary(
                TransactionCheckOutDialogSummary.RowModel("exchange rate", "25$"),
                "Recipient may receive less due ...",
                TransactionCheckOutDialogSummary.Receipt(
                    TransactionCheckOutDialogSummary.RowModel("exchange rate", "25$"),
                    TransactionCheckOutDialogSummary.RowModel("exchange rate", "25$"),
                    TransactionCheckOutDialogSummary.RowModel("exchange rate", "25$"),
                ),
                TransactionCheckOutDialogSummary.Sender(
                    TransactionCheckOutDialogSummary.RowModel("exchange rate", "25$"),
                    TransactionCheckOutDialogSummary.RowModel("exchange rate", "25$"),
                    TransactionCheckOutDialogSummary.RowModel("exchange rate", "25$"),
                    TransactionCheckOutDialogSummary.RowModel("exchange rate", "25$"),
                ),
            ),
            Transaction(),
            { },
            { },
            { },
            { },
            { _, _ -> },
        ),
    )
}
