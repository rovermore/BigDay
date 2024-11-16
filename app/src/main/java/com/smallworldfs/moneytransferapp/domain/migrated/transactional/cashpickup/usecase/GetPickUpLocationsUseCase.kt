package com.smallworldfs.moneytransferapp.domain.migrated.transactional.cashpickup.usecase

import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseUseCase
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.CashPickUpLocationsResponse
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.RequestCashPickUpLocationsDataModel
import com.smallworldfs.moneytransferapp.domain.migrated.transactional.cashpickup.repository.CashPickUpRepository
import javax.inject.Inject

class GetPickUpLocationsUseCase @Inject constructor(private val cashPickUpRepository: CashPickUpRepository) : BaseUseCase<RequestCashPickUpLocationsDataModel, CashPickUpLocationsResponse?>() {

    override suspend fun useCaseFunction(input: RequestCashPickUpLocationsDataModel): CashPickUpLocationsResponse? {
        return cashPickUpRepository.requestCashPickupLocationsAsync(input)
    }
}
