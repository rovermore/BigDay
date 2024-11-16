package com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model

import com.google.android.gms.maps.model.LatLng
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickUpMarkerPresentationModel
import com.smallworldfs.moneytransferapp.utils.DOUBLE_ZERO
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.Log
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class ResponseCashPickUpLocationDataModel(
    var latitude: String? = STRING_EMPTY,
    var longitude: String? = STRING_EMPTY,
    var locationState: String? = STRING_EMPTY,
    var locationCity: String? = STRING_EMPTY,
    var representativeName: String? = STRING_EMPTY,
    var locationName: String? = STRING_EMPTY,
    var locationAddress: String? = STRING_EMPTY,
    var locationCode: String? = STRING_EMPTY,
    var representativeCode: Int? = INT_ZERO,
    var fee: Double? = DOUBLE_ZERO,
    var rate: String? = STRING_EMPTY,
    var deliveryTime: String? = STRING_EMPTY
) {

    fun toCashPickUpMarkerPresentationModel(): CashPickUpMarkerPresentationModel {

        val location = if (!latitude.isNullOrEmpty() && !longitude.isNullOrEmpty()) {
            try {
                val lat = latitude?.toDouble()
                val long = longitude?.toDouble()

                if (lat != null && long != null) {
                    LatLng(lat, long)
                } else {
                    LatLng(DOUBLE_ZERO, DOUBLE_ZERO)
                }
            } catch (e: Exception) {
                Log.e("Error parsing", "The point $this cannot be parsed. Exception: $e")
                LatLng(DOUBLE_ZERO, DOUBLE_ZERO)
            }
        } else {
            LatLng(DOUBLE_ZERO, DOUBLE_ZERO)
        }

        return CashPickUpMarkerPresentationModel(
            location = location,
            locationState = this.locationState,
            locationCity = this.locationCity,
            representativeName = this.representativeName,
            locationName = this.locationName,
            locationAddress = this.locationAddress,
            locationCode = this.locationCode,
            representativeCode = this.representativeCode,
            fee = this.fee,
            rate = this.rate,
            deliveryTime = this.deliveryTime,
            selected = false
        )
    }
}
