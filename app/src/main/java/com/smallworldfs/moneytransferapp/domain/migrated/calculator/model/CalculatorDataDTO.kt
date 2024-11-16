package com.smallworldfs.moneytransferapp.domain.migrated.calculator.model

import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateValues
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class CalculatorDataDTO(
    val originCountry: CountryDTO = CountryDTO(),
    val payoutCountry: CountryDTO = CountryDTO(),
    val sendingCurrency: Currency = Currency(),
    val payoutCurrency: Currency = Currency(),
    val listMethods: List<Method> = emptyList(),
    val deliveryMethod: DeliveryMethodDTO = DeliveryMethodDTO(),
    var amount: String = STRING_EMPTY,
    val payoutMethod: Method? = null,
    val operationType: String = STRING_EMPTY,
    val beneficiaryId: String = STRING_EMPTY,
    val youPay: String = Constants.CALCULATOR.DEFAULT_AMOUNT,
    val representativeCode: String = STRING_EMPTY,
    val currentCalculator: RateValues? = null,
    val beneficiaryType: String = STRING_EMPTY,
    var currencyType: String = STRING_EMPTY
)

data class Currency(val countryIso3: String = STRING_EMPTY, val isoCode: String = STRING_EMPTY)
