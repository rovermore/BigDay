package com.smallworldfs.moneytransferapp.data.transactional.cashpickup.repository.network

import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.CashPickUpLocationsResponse
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.ResponseCashPickUpChooseLocationDataModel
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CashPickUpService {

    @GET(EndPoint.REPRESENTATIVES_FILTER)
    fun requestCashPickupLocationsAsync(@Query("userToken") userToken: String, @Query("userId") userId: String, @Query("amount") amount: String, @Query("currencieType") currencyType: String, @Query("currencieOrigin") currencyOrigin: String, @Query("beneficiaryId") beneficiaryId: String): Deferred<CashPickUpLocationsResponse>

    @POST(EndPoint.REPRESENTATIVE)
    fun requestCashPickupChooseLocationAsync(@Query("userId") userId: String, @Query("userToken") userToken: String, @Query("deliveryMethod") deliveryMethod: String, @Query("locationCode") locationCode: String, @Query("representativeCode") representativeCode: String, @Query("beneficiaryId") beneficiaryId: String, @Query("pick_up_fee") pickUpFee: String, @Query("pick_up_rate") pickUpRate: String): Deferred<ResponseCashPickUpChooseLocationDataModel>
}
