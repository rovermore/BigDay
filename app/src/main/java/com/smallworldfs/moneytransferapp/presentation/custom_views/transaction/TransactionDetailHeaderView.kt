package com.smallworldfs.moneytransferapp.presentation.custom_views.transaction

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.util.Pair
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadCircularImage
import com.smallworldfs.moneytransferapp.databinding.StatusDetailHeaderViewBinding
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.AmountFormatter
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.visible
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionDetailHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var binding: StatusDetailHeaderViewBinding? = null

    init {
        binding = StatusDetailHeaderViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true,
        )
    }

    fun bind(transaction: TransactionUIModel) {

        val beneficiaryName = formatBeneficiaryName(transaction)
        val formattedAmountAndCurrency = formatAmountWithCurrency(transaction)
        val beneficiaryCountry = transaction.beneficiaryCountry
        val beneficiaryFirstCharacter = if (transaction.beneficiaryFirstName.isNotEmpty()) transaction.beneficiaryFirstName.substring(0, 1).uppercase(Locale.getDefault()) else ""
        val beneficiarySecondCharacter = if (transaction.beneficiaryLastName.isNotEmpty()) transaction.beneficiaryLastName.substring(0, 1).uppercase(Locale.getDefault()) else ""
        val deliveryMethod = transaction.translatedDeliveryMethod
        val monthDayPair = formatTransactionDate(transaction)

        with(binding!!) {
            if (beneficiaryName.isNotEmpty()) {
                userNameTextview.text = beneficiaryName
            } else {
                userNameTextview.text = "-"
            }

            if (formattedAmountAndCurrency.isNotEmpty()) {
                checkoutAmountTextview.text = formattedAmountAndCurrency
            } else {
                checkoutAmountTextview.text = "-"
            }

            countryFlag.loadCircularImage(
                context,
                R.drawable.placeholder_country_adapter,
                Constants.COUNTRY.FLAG_IMAGE_ASSETS + beneficiaryCountry + Constants.COUNTRY.FLAG_IMAGE_EXTENSION,
            )

            userNameLetterText.text = beneficiaryFirstCharacter
            userNameLetterText2.text = beneficiarySecondCharacter
            deliveryMethodTextview.text = deliveryMethod

            if (monthDayPair.first.isNotEmpty() && monthDayPair.second.isNotEmpty()) {
                numberMonthTransaction.text = monthDayPair.first
                numberDayTransaction.text = monthDayPair.second
            }

            topCardviewHeader.visible()
        }
    }

    private fun formatBeneficiaryName(transaction: TransactionUIModel): String {
        return if (!TextUtils.isEmpty(transaction.beneficiaryFirstName)) {
            transaction.beneficiaryFirstName + " " + transaction.beneficiaryLastName
        } else ""
    }

    private fun formatAmountWithCurrency(transaction: TransactionUIModel): String {
        return AmountFormatter.formatDoubleAmountNumber(transaction.payoutPrincipal.toDouble()) + " " + transaction.payoutCurrency
    }

    private fun formatTransactionDate(transaction: TransactionUIModel): Pair<String, String> {
        var month = SimpleDateFormat("MMM", Locale.getDefault()).format(transaction.createdAt.time)
        var day = transaction.createdAt.get(Calendar.DAY_OF_MONTH)
        return Pair(month.toString(), day.toString())
    }
}
