package com.smallworldfs.moneytransferapp.presentation.common.coordinates

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

sealed class SWCoordinates(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
) : Serializable {
    class LatitudeLongitude(latitude: Double, longitude: Double) : SWCoordinates(latitude, longitude)
    object NotDefined : SWCoordinates()

    fun toLatLang() = LatLng(latitude, longitude)
}
