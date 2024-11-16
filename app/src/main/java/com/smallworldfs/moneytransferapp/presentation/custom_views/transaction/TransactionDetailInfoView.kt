package com.smallworldfs.moneytransferapp.presentation.custom_views.transaction

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.TransactionDetailInfoViewBinding
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionItemValue
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionItemValueUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.AmountFormatter
import com.smallworldfs.moneytransferapp.utils.AnimationUtils
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView

class TransactionDetailInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var binding: TransactionDetailInfoViewBinding? = null
    private var eventAction: (String) -> Unit = {}

    init {
        binding = TransactionDetailInfoViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true,
        )
    }

    fun setEventListener(eventAction: (String) -> Unit) {
        this.eventAction = eventAction
    }

    fun bind(transaction: TransactionUIModel) {
        binding?.let { binding ->
            var subtotal = STRING_EMPTY
            var feeFormatted = STRING_EMPTY
            var totalToPay = STRING_EMPTY
            val transactionTaxesList = ArrayList<TransactionItemValue>()
            var promotionAmount = ""

            if (transaction.sendingCurrency == "USD") {
                if (transaction.totalSale.isNotEmpty()) {
                    subtotal = AmountFormatter.formatDoubleAmountNumber(transaction.payoutPrincipal.toDouble()) + " " + transaction.payoutCurrency
                    totalToPay = "$" + AmountFormatter.formatDoubleAmountNumber(transaction.totalSale.toDouble())
                }

                if (transaction.fixedFee.isNotEmpty() && transaction.variableFee.isNotEmpty()) {
                    feeFormatted = "$" + AmountFormatter.formatDoubleAmountNumber(transaction.fixedFee.toDouble() + transaction.variableFee.toDouble())
                }

                if (transaction.taxCode.isNotEmpty() && transaction.taxAmount.isNotEmpty()) {
                    transactionTaxesList.add(TransactionItemValue(transaction.taxCode, "$" + AmountFormatter.formatDoubleAmountNumber(transaction.taxAmount.toDouble())))
                }

                if (transaction.transactionTaxes.title.isNotEmpty() && transaction.transactionTaxes.value.isNotEmpty()) {
                    transactionTaxesList.add(
                        TransactionItemValue(
                            transaction.transactionTaxes.title,
                            "$" + AmountFormatter.formatDoubleAmountNumber(transaction.transactionTaxes.value.toDouble()),
                        ),
                    )
                }

                promotionAmount = if (transaction.promotionAmount.isEmpty()) "" else "- " + "$" + AmountFormatter.formatDoubleAmountNumber(transaction.promotionAmount.toDouble())
            } else {
                if (transaction.totalSale.isNotEmpty()) {
                    subtotal = AmountFormatter.formatDoubleAmountNumber(transaction.payoutPrincipal.toDouble()) + " " + transaction.payoutCurrency
                    totalToPay = AmountFormatter.formatDoubleAmountNumber(transaction.totalSale.toDouble()) + " " + transaction.sendingCurrency
                }

                if (transaction.fixedFee.isNotEmpty() && transaction.variableFee.isNotEmpty()) {
                    feeFormatted = AmountFormatter.formatDoubleAmountNumber(transaction.fixedFee.toDouble() + transaction.variableFee.toDouble()) + " " + transaction.sendingCurrency
                }

                if (transaction.taxCode.isNotEmpty() && transaction.taxAmount.isNotEmpty()) {
                    transactionTaxesList.add(
                        TransactionItemValue(
                            transaction.taxCode,
                            AmountFormatter.formatDoubleAmountNumber(transaction.taxAmount.toDouble()) + " " + transaction.sendingCurrency,
                        ),
                    )
                }
                promotionAmount = if (transaction.promotionAmount.isEmpty()) "" else "- " + AmountFormatter.formatDoubleAmountNumber(transaction.promotionAmount.toDouble()) + " " + transaction.sendingCurrency
            }
            var deliveryInformation = getDeliveryInformation(transaction)
            var transactionInformation = getTransactionInformation(transaction)

            // If the user country is USA change the string names to CFPB terminology
            if (transaction.senderCountry == Constants.COUNTRY.US_COUNTRY_VALUE) {
                binding.discountLabel.text = context.getString(R.string.discount_label_usa)
                binding.subtotalTextviewKey.text = context.getString(R.string.subtotal_usa)
                binding.transferFeeTextviewKey.text = context.getString(R.string.fee_text_usa)
                binding.totalPayTextviewTitle.text = context.getString(R.string.total_to_pay_usa)
                deliveryInformation = transaction.deliveryInformation
                transactionInformation = transaction.transactionInformation
            } else {
                binding.discountLabel.text = context.getString(R.string.discount_label)
            }

            binding.subtotalTextview.text = subtotal
            binding.feeTextview.text = feeFormatted

            // Inflate transaction taxes
            if (transactionTaxesList.isNotEmpty()) {
                for (item in transactionTaxesList) {
                    val taxTitle = createStyledTextView(item.title)
                    val taxValue = createStyledTextView(item.getValue())
                    binding.taxesLabelLayout.addView(taxTitle)
                    binding.taxesValuesLayout.addView(taxValue)
                }
            }

            binding.totalPayTextview.text = totalToPay

            if (transaction.paid || transaction.status == Constants.TRANSACTION_STATUS.CLOSED_PAID_OUT) {
                binding.totalPayTextviewTitle.text = context.getString(R.string.beneficiary_detail_activity_total_paid)
            }

            binding.deliveryMethodAppendContainer.removeAllViews()

            if (deliveryInformation.isNotEmpty()) {
                for ((position, item) in deliveryInformation.withIndex()) {
                    val transactionItemDetail = LayoutInflater.from(context).inflate(R.layout.checkout_item_transaction_details_layout, null, true) as LinearLayout
                    (transactionItemDetail.findViewById<View>(R.id.title) as StyledTextView).text = item.title
                    (transactionItemDetail.findViewById<View>(R.id.value) as StyledTextView).text = item.value
                    if (position % 2 == 0) {
                        transactionItemDetail.findViewById<View>(R.id.top_item_container).setBackgroundColor(ContextCompat.getColor(context, R.color.default_grey_light_background))
                    } else {
                        transactionItemDetail.findViewById<View>(R.id.top_item_container).setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    }

                    binding.deliveryMethodAppendContainer.addView(transactionItemDetail)
                }
            }

            binding.transactionAppendContainer.removeAllViews()

            if (transactionInformation.isNotEmpty()) {
                for ((position, item) in transactionInformation.withIndex()) {
                    val transactionItemDetail = LayoutInflater.from(context).inflate(R.layout.checkout_item_transaction_details_layout, null, true) as LinearLayout
                    (transactionItemDetail.findViewById<View>(R.id.title) as StyledTextView).text = item.title
                    (transactionItemDetail.findViewById<View>(R.id.value) as StyledTextView).text = item.value
                    if (position % 2 == 0) {
                        transactionItemDetail.findViewById<View>(R.id.top_item_container).setBackgroundColor(ContextCompat.getColor(context, R.color.default_grey_light_background))
                    } else {
                        transactionItemDetail.findViewById<View>(R.id.top_item_container).setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    }
                    binding.transactionAppendContainer.addView(transactionItemDetail)
                }
            }

            binding.transactionInfoCardview.visible()

            if (promotionAmount.isNullOrEmpty()) {
                // Disable row promotion
                binding.discountLabel.gone()
                binding.discountTextview.gone()
            } else {
                binding.discountTextview.text = promotionAmount
            }

            binding.moreButtonExpand.setOnClickListener {
                binding.expandableLayout.toggle(true)
                if (binding.expandableLayout.isExpanded) {
                    eventAction.invoke("click_more")
                    AnimationUtils.rotateView(binding.expandCollapseArrow, 300, 0)
                    binding.moreButtonText.text = context.getString(R.string.less_text_button)
                } else {
                    eventAction.invoke("click_less")
                    AnimationUtils.rotateView(binding.expandCollapseArrow, 300, 180)
                    binding.moreButtonText.text = context.getString(R.string.more_text_button)
                }
            }
        }
    }

    private fun getDeliveryInformation(transaction: TransactionUIModel): List<TransactionItemValueUIModel> {
        val list = mutableListOf<TransactionItemValueUIModel>()
        when (transaction.deliveryMethod) {
            Constants.DELIVERY_METHODS.CASH_PICKUP -> {
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_city), if (TextUtils.isEmpty(transaction.beneficiaryCity)) "-" else transaction.beneficiaryCity))
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_country), if (TextUtils.isEmpty(transaction.beneficiaryCountry)) "-" else transaction.beneficiaryCountry))
            }
            Constants.DELIVERY_METHODS.BANK_DEPOSIT -> {
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_country), if (TextUtils.isEmpty(transaction.beneficiaryCountry)) "-" else transaction.beneficiaryCountry))
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_bank), if (TextUtils.isEmpty(transaction.beneficiaryBankName)) "-" else transaction.beneficiaryBankName))
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_account_number), if (TextUtils.isEmpty(transaction.beneficiaryAccountNumber)) "-" else transaction.beneficiaryAccountNumber))
            }
            Constants.DELIVERY_METHODS.PHYSICAL_DELIVERY -> {
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_address), if (TextUtils.isEmpty(transaction.beneficiaryAddress)) "-" else transaction.beneficiaryAddress))
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_postal_code), if (TextUtils.isEmpty(transaction.beneficiaryZip)) "-" else transaction.beneficiaryZip))
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_city), if (TextUtils.isEmpty(transaction.beneficiaryCity)) "-" else transaction.beneficiaryCity))
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_country), if (TextUtils.isEmpty(transaction.beneficiaryCountry)) "-" else transaction.beneficiaryCountry))
            }
            Constants.DELIVERY_METHODS.TOP_UP, Constants.DELIVERY_METHODS.MOBILE_WALLET -> {
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_country), if (TextUtils.isEmpty(transaction.beneficiaryCountry)) "-" else transaction.beneficiaryCountry))
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_mobile), if (TextUtils.isEmpty(transaction.beneficiaryMobilePhoneNumber)) "-" else transaction.beneficiaryMobilePhoneNumber))
            }
            Constants.DELIVERY_METHODS.CASH_CARD_RELOAD -> {
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_country), if (TextUtils.isEmpty(transaction.beneficiaryCountry)) "-" else transaction.beneficiaryCountry))
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_card_type), if (TextUtils.isEmpty(transaction.bankAccountType)) "-" else transaction.bankAccountType))
                list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_account_number), if (TextUtils.isEmpty(transaction.beneficiaryAccountNumber)) "-" else transaction.beneficiaryAccountNumber))
            }
            else -> {}
        }
        return list
    }

    private fun getTransactionInformation(transaction: TransactionUIModel): List<TransactionItemValueUIModel> {
        val list = mutableListOf<TransactionItemValueUIModel>()
        list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_mtn_code), if (TextUtils.isEmpty(transaction.mtn)) "-" else transaction.mtn))
        list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_payer), if (TextUtils.isEmpty(transaction.payerName)) "-" else transaction.payerName))
        list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_rate), AmountFormatter.formatDoubleRateNumber(transaction.rate)))
        list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_payment_method), if (TextUtils.isEmpty(transaction.translatedPaymentMethod)) "-" else transaction.translatedPaymentMethod))
        list.add(TransactionItemValueUIModel(context.getString(R.string.status_transfer_show_more_promotion_applied), if (TextUtils.isEmpty(transaction.promotionName)) "-" else transaction.promotionName))
        return list
    }

    private fun createStyledTextView(title: String): StyledTextView =
        StyledTextView(context).apply {
            setPaddingRelative(0, 0, 0, 0)
            ellipsize = TextUtils.TruncateAt.END
            maxLines = 1
            includeFontPadding = false
            textSize = 13f
            text = title
            setTextColor(ContextCompat.getColor(context, R.color.default_text_color))
            setTypeface(typeface, Typeface.BOLD)
            gravity = Gravity.END
        }
}
