package com.smallworldfs.moneytransferapp.domain.migrated.mtn.repository

import com.smallworldfs.moneytransferapp.domain.migrated.mtn.model.MtnStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

interface MTNRepository {
    fun getMtnStatus(mtn: String, country: String): OperationResult<MtnStatusDTO, Error>
}
