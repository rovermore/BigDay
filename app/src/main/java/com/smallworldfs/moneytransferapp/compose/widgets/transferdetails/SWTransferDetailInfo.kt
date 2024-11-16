package com.smallworldfs.moneytransferapp.compose.widgets.transferdetails

import android.text.TextUtils
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyControl
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.ComplianceDocDTO
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionItemValue
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionInfoItemValueUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionItemValueUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.AmountFormatter
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWTransferDetailInfo(
    transaction: TransactionUIModel,
    registerEventCallBack: (String) -> Unit
) {

    val expanded = rememberSaveable { mutableStateOf(false) }
    val expandButtonText = rememberSaveable { mutableIntStateOf(R.string.more_text_button) }
    val rotation = rememberSaveable { mutableFloatStateOf(90f) }

    var subtotal = STRING_EMPTY
    var totalToPay = STRING_EMPTY
    val transactionTaxesList = ArrayList<TransactionItemValue>()
    var feeFormatted = STRING_EMPTY
    val promotionAmountFormatted: String
    val deliveryInformationFormatted: List<TransactionItemValueUIModel>
    val transactionInformationFormatted: List<TransactionItemValueUIModel>
    val subtotalTitle: String
    val feeTitle: String
    val discountTitle: String
    val totalToPayTitle: String

    with(transaction) {
        if (sendingCurrency == "USD") {
            if (totalSale.isNotEmpty()) {
                subtotal = AmountFormatter.formatDoubleAmountNumber(payoutPrincipal.toDouble()) + " " + payoutCurrency
                totalToPay = "$" + AmountFormatter.formatDoubleAmountNumber(totalSale.toDouble())
            }

            if (fixedFee.isNotEmpty() && variableFee.isNotEmpty()) {
                feeFormatted = "$" + AmountFormatter.formatDoubleAmountNumber(fixedFee.toDouble() + variableFee.toDouble())
            }

            if (taxCode.isNotEmpty() && taxAmount.isNotEmpty()) {
                transactionTaxesList.add(TransactionItemValue(taxCode, "$" + AmountFormatter.formatDoubleAmountNumber(taxAmount.toDouble())))
            }

            if (transactionTaxes.title.isNotEmpty() && transactionTaxes.value.isNotEmpty()) {
                transactionTaxesList.add(
                    TransactionItemValue(
                        transactionTaxes.title,
                        "$" + AmountFormatter.formatDoubleAmountNumber(transactionTaxes.value.toDouble()),
                    ),
                )
            }

            promotionAmountFormatted = if (promotionAmount.isEmpty()) "" else "- " + "$" + AmountFormatter.formatDoubleAmountNumber(promotionAmount.toDouble())
        } else {
            if (totalSale.isNotEmpty()) {
                subtotal = AmountFormatter.formatDoubleAmountNumber(payoutPrincipal.toDouble()) + " " + payoutCurrency
                totalToPay = AmountFormatter.formatDoubleAmountNumber(totalSale.toDouble()) + " " + sendingCurrency
            }

            if (fixedFee.isNotEmpty() && variableFee.isNotEmpty()) {
                feeFormatted = AmountFormatter.formatDoubleAmountNumber(fixedFee.toDouble() + variableFee.toDouble()) + " " + sendingCurrency
            }

            if (taxCode.isNotEmpty() && taxAmount.isNotEmpty()) {
                transactionTaxesList.add(
                    TransactionItemValue(
                        taxCode,
                        AmountFormatter.formatDoubleAmountNumber(taxAmount.toDouble()) + " " + sendingCurrency,
                    ),
                )
            }
            promotionAmountFormatted = if (promotionAmount.isEmpty()) "" else "- " + AmountFormatter.formatDoubleAmountNumber(promotionAmount.toDouble()) + " " + sendingCurrency
        }

        if (senderCountry == Constants.COUNTRY.US_COUNTRY_VALUE) {
            discountTitle = stringResource(id = R.string.discount_label_usa)
            subtotalTitle = stringResource(id = R.string.subtotal_usa)
            feeTitle = stringResource(id = R.string.fee_text_usa)
            totalToPayTitle = stringResource(id = R.string.total_to_pay_usa)
            deliveryInformationFormatted = deliveryInformation
            transactionInformationFormatted = transactionInformation
        } else {
            discountTitle = stringResource(id = R.string.discount_label)
            subtotalTitle = stringResource(id = R.string.subtotal)
            feeTitle = stringResource(id = R.string.fee_text)
            totalToPayTitle = stringResource(id = R.string.total_to_pay)
            deliveryInformationFormatted = getDeliveryInformation(this)
                .map { TransactionItemValueUIModel(title = stringResource(id = it.titleId), value = it.value) }
            transactionInformationFormatted = getTransactionInformation(this)
                .map { TransactionItemValueUIModel(title = stringResource(id = it.titleId), value = it.value) }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            horizontalAlignment = Alignment.End,
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                ) {
                    Row {
                        SWText(
                            modifier = Modifier
                                .weight(2f),
                            text = subtotalTitle,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.End,
                        )
                        SWText(
                            modifier = Modifier
                                .weight(1f),
                            text = subtotal,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.End,
                        )
                    }

                    Row {
                        SWText(
                            modifier = Modifier
                                .weight(2f),
                            text = feeTitle,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.End,
                        )
                        SWText(
                            modifier = Modifier
                                .weight(1f),
                            text = feeFormatted,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.End,
                        )
                    }

                    transactionTaxesList.forEach {
                        Row {
                            SWText(
                                modifier = Modifier
                                    .weight(2f),
                                text = it.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.End,
                            )
                            SWText(
                                modifier = Modifier
                                    .weight(1f),
                                text = it.getValue(),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.End,
                            )
                        }
                    }

                    Row {
                        if (promotionAmountFormatted.isNotEmpty()) {
                            SWText(
                                modifier = Modifier
                                    .weight(2f),
                                text = discountTitle,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.End,
                            )
                            SWText(
                                modifier = Modifier
                                    .weight(1f),
                                text = promotionAmountFormatted,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.End,
                            )
                        }
                    }
                    Row {
                        if (transaction.paid || transaction.status == Constants.TRANSACTION_STATUS.CLOSED_PAID_OUT) {
                            SWText(
                                modifier = Modifier
                                    .weight(2f),
                                text = stringResource(id = R.string.beneficiary_detail_activity_total_paid),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = black,
                                textAlign = TextAlign.End,
                            )
                        } else {
                            SWText(
                                modifier = Modifier
                                    .weight(2f),
                                text = totalToPayTitle,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = black,
                                textAlign = TextAlign.End,
                            )
                        }

                        SWText(
                            modifier = Modifier
                                .weight(1f),
                            text = totalToPay,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = black,
                            textAlign = TextAlign.End,
                        )
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { toggleTransferDetailsView(expanded, expandButtonText, rotation) { registerEventCallBack(it) } },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(1f))

                SWText(
                    text = stringResource(id = expandButtonText.intValue),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = blueAccentColor,
                )

                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(12.dp)
                        .rotate(rotation.floatValue),
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    tint = blueAccentColor,
                    contentDescription = null,
                )
            }

            if (expanded.value) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                ) {
                    SWText(
                        modifier = Modifier
                            .padding(start = 8.dp),
                        text = stringResource(id = R.string.delivery_information_label),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = black,
                    )

                    InformationList(items = deliveryInformationFormatted)

                    SWText(
                        modifier = Modifier
                            .padding(
                                top = 24.dp,
                                start = 8.dp,
                            ),
                        text = stringResource(id = R.string.transaction_information_label),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = black,
                    )

                    InformationList(items = transactionInformationFormatted)

                    Box(
                        modifier = Modifier
                            .height(48.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun InformationList(
    items: List<TransactionItemValueUIModel>,
) {
    items.forEachIndexed { index, item ->
        val backgroundColor = if (index % 2 == 0) defaultGreyControl else neutral0

        Row(
            modifier = Modifier
                .background(backgroundColor),
        ) {
            SWText(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = item.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            SWText(
                modifier = Modifier
                    .padding(end = 8.dp),
                text = item.value,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            )
        }
    }
}

private fun getDeliveryInformation(
    transaction: TransactionUIModel
): List<TransactionInfoItemValueUIModel> {
    val list = mutableListOf<TransactionInfoItemValueUIModel>()

    with(transaction) {
        when (deliveryMethod) {
            Constants.DELIVERY_METHODS.CASH_PICKUP -> {
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_city, if (TextUtils.isEmpty(beneficiaryCity)) "-" else beneficiaryCity))
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_country, if (TextUtils.isEmpty(beneficiaryCountry)) "-" else beneficiaryCountry))
            }

            Constants.DELIVERY_METHODS.BANK_DEPOSIT -> {
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_country, if (TextUtils.isEmpty(beneficiaryCountry)) "-" else beneficiaryCountry))
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_bank, if (TextUtils.isEmpty(beneficiaryBankName)) "-" else beneficiaryBankName))
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_account_number, if (TextUtils.isEmpty(beneficiaryAccountNumber)) "-" else beneficiaryAccountNumber))
            }

            Constants.DELIVERY_METHODS.PHYSICAL_DELIVERY -> {
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_address, if (TextUtils.isEmpty(beneficiaryAddress)) "-" else beneficiaryAddress))
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_postal_code, if (TextUtils.isEmpty(beneficiaryZip)) "-" else beneficiaryZip))
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_city, if (TextUtils.isEmpty(beneficiaryCity)) "-" else beneficiaryCity))
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_country, if (TextUtils.isEmpty(beneficiaryCountry)) "-" else beneficiaryCountry))
            }

            Constants.DELIVERY_METHODS.TOP_UP, Constants.DELIVERY_METHODS.MOBILE_WALLET -> {
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_country, if (TextUtils.isEmpty(beneficiaryCountry)) "-" else beneficiaryCountry))
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_mobile, if (TextUtils.isEmpty(beneficiaryMobilePhoneNumber)) "-" else beneficiaryMobilePhoneNumber))
            }

            Constants.DELIVERY_METHODS.CASH_CARD_RELOAD -> {
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_country, if (TextUtils.isEmpty(beneficiaryCountry)) "-" else beneficiaryCountry))
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_card_type, if (TextUtils.isEmpty(bankAccountType)) "-" else bankAccountType))
                list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_account_number, if (TextUtils.isEmpty(beneficiaryAccountNumber)) "-" else beneficiaryAccountNumber))
            }

            else -> {}
        }
    }

    return list
}

private fun getTransactionInformation(
    transaction: TransactionUIModel
): List<TransactionInfoItemValueUIModel> {
    val list = mutableListOf<TransactionInfoItemValueUIModel>()

    with(transaction) {
        list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_mtn_code, if (TextUtils.isEmpty(ComplianceDocDTO.Other.mtn)) "-" else ComplianceDocDTO.Other.mtn))
        list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_payer, if (TextUtils.isEmpty(payerName)) "-" else payerName))
        list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_rate, AmountFormatter.formatDoubleRateNumber(rate)))
        list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_payment_method, if (TextUtils.isEmpty(translatedPaymentMethod)) "-" else translatedPaymentMethod))
        list.add(TransactionInfoItemValueUIModel(R.string.status_transfer_show_more_promotion_applied, if (TextUtils.isEmpty(promotionName)) "-" else promotionName))
    }

    return list
}

private fun toggleTransferDetailsView(
    expand: MutableState<Boolean>,
    expandText: MutableState<Int>,
    rotation: MutableState<Float>,
    registerEventCallBack: (String) -> Unit
) {
    if (expand.value) {
        registerEventCallBack("click_less")
        expandText.value = R.string.more_text_button
        rotation.value = 90f
    } else {
        registerEventCallBack("click_more")
        expandText.value = R.string.less_text_button
        rotation.value = 270f
    }

    expand.value = !expand.value
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun TransactionStatusDetailLayoutPreview() {
    SWTransferDetailInfo(
        transaction = TransactionUIModel(
            status = Constants.TRANSACTION_STATUS.NEW,
            beneficiaryCountry = "ARG",
            payoutPrincipal = "22354",
            payoutCurrency = "ARS",
            mtn = "28746238743",
            fixedFee = "5",
            variableFee = "2",
            totalSale = "465",
            promotionAmount = "6.4",
            sendingCurrency = "EUR",
            paid = false,
            deliveryMethod = "PHYSICAL_DELIVERY",
            beneficiaryBankName = "Santander",
            beneficiaryAccountNumber = "432876238746",
            beneficiaryAddress = "Address",
            beneficiaryZip = "Zip",
            beneficiaryMobilePhoneNumber = "2387623",
            beneficiaryCity = "City",
            bankAccountType = "Type",
            rate = 10.0,
            payerName = "Payer",
            translatedPaymentMethod = "Method",
            promotionName = "Promotion",
            senderCountry = "ARG",
            deliveryInformation = listOf(
                TransactionItemValueUIModel("Title", "Description"),
                TransactionItemValueUIModel("Title", "Description"),
            ),
            transactionInformation = listOf(
                TransactionItemValueUIModel("Title", "Description"),
                TransactionItemValueUIModel("Title", "Description"),
                TransactionItemValueUIModel("Title", "Description"),
                TransactionItemValueUIModel("Title", "Description"),
                TransactionItemValueUIModel("Title", "Description"),
            ),
            taxCode = "Tax code",
            taxAmount = "22",
            transactionTaxes = TransactionItemValueUIModel("Tax", "23.67"),
        ),
        registerEventCallBack = {},
    )
}
