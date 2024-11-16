package com.smallworldfs.moneytransferapp.domain.migrated.transactional.cashpickup.usecase

import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseUseCase
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.RequestCashPickUpChooseLocationDataModel
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.ResponseCashPickUpChooseLocationDataModel
import com.smallworldfs.moneytransferapp.domain.migrated.transactional.cashpickup.repository.CashPickUpRepository
import javax.inject.Inject

class SetTheChoosePickUpLocationsUseCase @Inject constructor(private val cashPickUpRepository: CashPickUpRepository) : BaseUseCase<RequestCashPickUpChooseLocationDataModel, ResponseCashPickUpChooseLocationDataModel?>() {

    override suspend fun useCaseFunction(input: RequestCashPickUpChooseLocationDataModel): ResponseCashPickUpChooseLocationDataModel? {
        return cashPickUpRepository.requestCashPickupChooseLocationAsync(input)
    }
}
