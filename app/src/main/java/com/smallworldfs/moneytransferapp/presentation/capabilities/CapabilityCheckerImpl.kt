package com.smallworldfs.moneytransferapp.presentation.capabilities

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CapabilityCheckerImpl @Inject constructor(private val context: Context) : CapabilityChecker {

    override fun hasBiometricCapability() = BiometricManager.from(context)
        .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS

    override fun askForLocationPermissions(): OperationResult<Boolean, Error> {
        val deferred = CompletableDeferred<OperationResult<Boolean, Error>>()
        Dexter.withContext(context)
            .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        val allGranted = report.areAllPermissionsGranted()
                        if (allGranted) deferred.complete(Success(true))
                        else deferred.complete(Failure(Error.LocationUnavailable()))
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                },
            ).check()
        return runBlocking {
            deferred.await()
        }
    }

    private fun getReadWritePermissionCodes(): List<String> {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return permissions.toList()
    }

    override fun askForReadWritePermissions(): OperationResult<Boolean, Error> {
        val deferred = CompletableDeferred<OperationResult<Boolean, Error>>()
        Dexter.withContext(context)
            .withPermissions(getReadWritePermissionCodes())
            .withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        val allGranted = report.areAllPermissionsGranted()
                        if (allGranted) deferred.complete(Success(true))
                        else deferred.complete(Failure(Error.LocationUnavailable()))
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                },
            ).check()
        return runBlocking {
            deferred.await()
        }
    }

    override fun requestPermissions(
        activity: AppCompatActivity,
        vararg permissions: String,
        onGrantedPermissions: (Boolean, List<PermissionGrantedResponse>) -> Unit
    ) {
        Dexter.withContext(activity)
            .withPermissions(*permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    onGrantedPermissions(report.areAllPermissionsGranted(), report.grantedPermissionResponses)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }
}
