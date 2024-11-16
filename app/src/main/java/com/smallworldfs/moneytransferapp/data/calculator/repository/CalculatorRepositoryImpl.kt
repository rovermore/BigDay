package com.smallworldfs.moneytransferapp.data.calculator.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.calculator.model.CalculateRateValues
import com.smallworldfs.moneytransferapp.data.calculator.network.CalculatorNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.model.DeliveryMethodDTO
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.repository.CalculatorRepository
import javax.inject.Inject

class CalculatorRepositoryImpl @Inject constructor(
    private val calculatorNetworkDatasource: CalculatorNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper,
) : CalculatorRepository {

    override fun getDeliveryMethods(): OperationResult<List<DeliveryMethodDTO>, Error> {
        return calculatorNetworkDatasource.getDeliveryMethods()
            .map {
                val deliveryMethodsDTOList = mutableListOf<DeliveryMethodDTO>()
                it.deliveryMethods.forEach { entry ->
                    deliveryMethodsDTOList.add(DeliveryMethodDTO(entry.key, entry.value))
                }
                return Success(deliveryMethodsDTOList)
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }

    override fun calculateRate(request: Map<String, String>): OperationResult<CalculateRateValues, Error> =
        calculatorNetworkDatasource.calculateRate(request)
            .map {
                return Success(it.result)
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
}
