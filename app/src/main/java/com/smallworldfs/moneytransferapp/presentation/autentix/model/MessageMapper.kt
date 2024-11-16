package com.smallworldfs.moneytransferapp.presentation.autentix.model

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.utils.parseJSON
import javax.inject.Inject

class MessageMapper @Inject constructor() {

    fun handleEvent(json: String?): OperationResult<AutentixMessage, Error.UncompletedOperation> {
        val result = json.parseJSON<AutentixMessage>()
        return result
    }
}
