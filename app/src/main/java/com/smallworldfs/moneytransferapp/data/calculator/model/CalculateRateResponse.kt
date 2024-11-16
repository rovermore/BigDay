package com.smallworldfs.moneytransferapp.data.calculator.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Taxes
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class CalculateRateResponse(
    val result: CalculateRateValues = CalculateRateValues()
)

data class CalculateRateValues(
    val payoutPrincipal: Double = 0.0,
    val payoutRepresentativeCode: Long = 0,
    val principal: Double = 0.0,
    val promotionAmount: Double = 0.0,
    val promotionName: String = STRING_EMPTY,
    val promotionNumber: String = STRING_EMPTY,
    val rate: Double = 0.0,
    val totalFee: Double = 0.0,
    @SerializedName("taxes")
    val taxes: Taxes? = null,
    val totalSale: Double = 0.0,
    val promotionType: String = STRING_EMPTY,
    val discount: Double = 0.0,
    val promotionCode: String = STRING_EMPTY,
    @SerializedName("automatic")
    val autoAssigned: Boolean = false
)
