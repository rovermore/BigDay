package com.smallworldfs.moneytransferapp.domain.migrated.capabilities

import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

interface CapabilityChecker {
    fun hasBiometricCapability(): Boolean
    fun askForLocationPermissions(): OperationResult<Boolean, Error>
    fun requestPermissions(
        activity: AppCompatActivity,
        vararg permissions: String,
        onGrantedPermissions: (Boolean, List<PermissionGrantedResponse>) -> Unit
    )

    fun askForReadWritePermissions(): OperationResult<Boolean, Error>
}
