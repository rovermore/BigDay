package com.smallworldfs.moneytransferapp.data.settings.local

import com.smallworldfs.moneytransferapp.data.common.datastore.AppConfigDataStore
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import javax.inject.Inject

class SettingsLocalDataSource @Inject constructor(
    private val appConfigDataStore: AppConfigDataStore
) {
    init {
        saveBrazeStatus(true)
    }

    companion object {
        const val BRAZE_STATUS = "BRAZE_STATUS"
    }

    fun saveBrazeStatus(brazeStatus: Boolean): OperationResult<Boolean, Error> {
        return appConfigDataStore.save(BRAZE_STATUS, brazeStatus)
    }

    fun getBrazeStatus(): OperationResult<Boolean, Error> {
        return appConfigDataStore.read(BRAZE_STATUS)
    }
}
