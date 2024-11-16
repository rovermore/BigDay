package com.smallworldfs.moneytransferapp.modules.checkout.domain.model

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.utils.AmountFormatter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionItemValue(var title: String, private var value: String) : Parcelable {

    fun getValue(): String {
        return formattedRateValue
    }

    fun setValue(value: String) {
        this.value = value
    }

    private val formattedRateValue: String
        get() = if (title == SmallWorldApplication.getStr(R.string.rate_value_text)) {
            try {
                val rate = value.toDouble()
                AmountFormatter.formatDoubleRateNumber(rate)
            } catch (e: NumberFormatException) {
                value
            }
        } else {
            value
        }
}
