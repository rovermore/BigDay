package com.smallworldfs.moneytransferapp.domain.migrated.version

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.version.models.Version
import javax.inject.Inject

class VersionChecker @Inject constructor() {

    fun checkAppVersion(localVersion: Version, versionForceUpdate: Version): OperationResult<OperationCheck, Error> {
        return try {
            if (localVersion < versionForceUpdate) Success(OperationCheck.FORCE)
            else Success(OperationCheck.NOTHING)
        } catch (ee: IllegalArgumentException) {
            Failure(Error.UncompletedOperation())
        }
    }

    enum class OperationCheck {
        FORCE, ASK, NOTHING
    }
}
