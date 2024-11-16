package com.smallworldfs.moneytransferapp.domain.model

sealed class Coordinates {
    class LatLon(val latitude: Double, val longitude: Double) : Coordinates()
    object InvalidCoordinates : Coordinates()
}
