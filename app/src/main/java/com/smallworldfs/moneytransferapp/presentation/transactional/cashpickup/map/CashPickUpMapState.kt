package com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map

import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.ResponseCashPickUpLocationDataModel
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickUpMarkerPresentationModel
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.io.Serializable

data class CashPickUpMapState(
    var amount: String = STRING_EMPTY,
    var currencyType: String = STRING_EMPTY,
    var currencyOrigin: String = STRING_EMPTY,
    var beneficiaryId: String = STRING_EMPTY,
    val allLocationResponses: List<ResponseCashPickUpLocationDataModel> = listOf(),
    val stateList: List<String> = listOf(),
    val cityList: List<String> = listOf(),
    val paymentNetworkList: List<String> = listOf(),
    val stateListSelected: Int = INT_ZERO,
    val cityListSelected: Int = INT_ZERO,
    val paymentNetworkListSelected: Int = INT_ZERO,
    val searchBoxValue: String = STRING_EMPTY,
    val locationSelected: CashPickUpMarkerPresentationModel? = null,
    var isAnyWherePickUp: Boolean = false
) : Serializable {

    constructor(amount: String, currencyType: String, currencyOrigin: String, beneficiaryId: String, isAnyWherePickUp: Boolean) : this() {
        this.amount = amount
        this.currencyType = currencyType
        this.currencyOrigin = currencyOrigin
        this.beneficiaryId = beneficiaryId
        this.isAnyWherePickUp = isAnyWherePickUp
    }
}
