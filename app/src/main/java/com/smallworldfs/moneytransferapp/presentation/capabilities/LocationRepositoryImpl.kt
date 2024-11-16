package com.smallworldfs.moneytransferapp.presentation.capabilities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.LocationRepository
import com.smallworldfs.moneytransferapp.presentation.common.coordinates.SWCoordinates
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(val context: Context) : LocationRepository {

    private inline fun locationPermissionEnabled(enabled: () -> Unit, disabled: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            enabled()
        } else {
            disabled()
        }
    }

    override fun getUserLocation(): OperationResult<SWCoordinates, Error> {
        val deferred = CompletableDeferred<OperationResult<SWCoordinates, Error>>()

        locationPermissionEnabled(
            enabled = { getCurrentLocation(deferred) },
            disabled = { deferred.complete(Failure(Error.LocationUnavailable())) }
        )

        return runBlocking {
            deferred.await()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(deferred: CompletableDeferred<OperationResult<SWCoordinates, Error>>) {
        LocationServices.getFusedLocationProviderClient(context).getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
                override fun isCancellationRequested() = false
            },
        ).addOnSuccessListener { location: Location? ->
            location?.let {
                deferred.complete(Success(SWCoordinates.LatitudeLongitude(it.latitude, it.longitude)))
            } ?: run {
                getLastKnownLocation(deferred)
            }
        }
            .addOnFailureListener {
                getLastKnownLocation(deferred)
            }
            .addOnCanceledListener {
                getLastKnownLocation(deferred)
            }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(deferred: CompletableDeferred<OperationResult<SWCoordinates, Error>>) {
        LocationServices.getFusedLocationProviderClient(context).lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    deferred.complete(Success(SWCoordinates.LatitudeLongitude(it.latitude, it.longitude)))
                } ?: run {
                    deferred.complete(Failure(Error.LocationUnavailable(STRING_EMPTY)))
                }
            }
            .addOnFailureListener {
                deferred.complete(Failure(Error.LocationUnavailable(STRING_EMPTY)))
            }
            .addOnCanceledListener {
                deferred.complete(Failure(Error.LocationUnavailable(STRING_EMPTY)))
            }
    }

    override fun calculateDistance(location1: SWCoordinates, location2: SWCoordinates): OperationResult<String, Error> {
        try {
            val point1 = Location(STRING_EMPTY)
            point1.apply {
                latitude = location1.latitude
                longitude = location1.longitude
            }
            val point2 = Location(STRING_EMPTY)
            point2.apply {
                latitude = location2.latitude
                longitude = location2.longitude
            }
            val distanceInMeters = point1.distanceTo(point2)
            if (distanceInMeters < 1000)
                return Success("$distanceInMeters m")
            else
                return Success(String.format("%.2f", distanceInMeters / 1000) + " km")
        } catch (e: Exception) {
            return Failure(Error.UncompletedOperation("Distance could not be calculated"))
        }
    }
}
