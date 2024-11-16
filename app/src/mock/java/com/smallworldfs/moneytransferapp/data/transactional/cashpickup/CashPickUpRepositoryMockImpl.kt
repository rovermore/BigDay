package com.smallworldfs.moneytransferapp.data.transactional.cashpickup

import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.CashPickUpLocationsResponse
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.RequestCashPickUpChooseLocationDataModel
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.RequestCashPickUpLocationsDataModel
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.ResponseCashPickUpChooseLocationDataModel
import com.smallworldfs.moneytransferapp.domain.migrated.transactional.cashpickup.repository.CashPickUpRepository
import javax.inject.Inject

class CashPickUpRepositoryMockImpl @Inject constructor() : CashPickUpRepository {

    override suspend fun requestCashPickupLocationsAsync(cashPickupLocationsRequestCashPickUpLocationsDataModel: RequestCashPickUpLocationsDataModel): CashPickUpLocationsResponse {
        return CashPickUpLocationsResponse()
    }

    override suspend fun requestCashPickupChooseLocationAsync(requestCashPickUpChooseLocationDataModel: RequestCashPickUpChooseLocationDataModel): ResponseCashPickUpChooseLocationDataModel {
        return ResponseCashPickUpChooseLocationDataModel()
    }

}