package com.smallworldfs.moneytransferapp.data.base

import com.smallworldfs.moneytransferapp.domain.migrated.calculator.model.CalculatorDataDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cache @Inject constructor() {

    var calculatorData: CalculatorDataDTO? = null

    fun updateCurrencyType(newCurrencyType: String) {
        calculatorData?.apply { currencyType = newCurrencyType }
    }
}
