package com.smallworldfs.moneytransferapp.presentation.location

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.Unit

class LocationListener(
    val activity: Activity,
    val lifecycle: Lifecycle,
    private val onLocationReceived: (Location) -> Unit,
    private val onError: () -> Unit
) : LifecycleObserver {

    private val fusedLocationClient: FusedLocationProviderClient
    private var state = DISCONNECTED

    init {
        lifecycle.addObserver(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun addLocationListener() {
        if (state == CONNECTED) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener(activity) { location ->
                    if (state == CONNECTED) {
                        location?.let {
                            onLocationReceived.invoke(it)
                        } ?: run {
                            onError.invoke()
                        }
                    }
                }.addOnFailureListener {
                    if (state == CONNECTED) {
                        onError.invoke()
                    }
                }.addOnCanceledListener {
                    if (state == CONNECTED) {
                        onError.invoke()
                    }
                }
            } else {
                if (state == CONNECTED) {
                    onError.invoke()
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeLocationListener() {
        state = DISCONNECTED
    }

    fun start() {
        state = CONNECTED
        addLocationListener()
    }

    fun stop() {
        removeLocationListener()
    }

    companion object {
        const val CONNECTED = "Connected"
        const val DISCONNECTED = "Disconnected"
    }
}
