package com.smallworldfs.moneytransferapp.domain.migrated.calculator.repository

import com.smallworldfs.moneytransferapp.data.calculator.model.CalculateRateValues
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.model.DeliveryMethodDTO

interface CalculatorRepository {

    fun getDeliveryMethods(): OperationResult<List<DeliveryMethodDTO>, Error>
    fun calculateRate(request: Map<String, String>): OperationResult<CalculateRateValues, Error>
}
