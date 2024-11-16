package com.smallworldfs.moneytransferapp.utils.widget

import com.google.android.gms.maps.MapView

interface MapProvider {
    fun getMap(): MapView?
}
