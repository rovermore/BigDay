package com.smallworldfs.moneytransferapp.domain.migrated.autentix.repository

import com.smallworldfs.moneytransferapp.data.autentix.model.AutentixSessionStatus
import com.smallworldfs.moneytransferapp.data.autentix.model.CheckSessionStatusParams
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.AutentixSessionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

interface AutentixRepository {
    fun getAutentixSessionURL(uuid: String, userToken: String, lang: String, faceCompare: Boolean, documentType: String): OperationResult<AutentixSessionDTO, Error>
    fun checkAutentixSessionStatus(params: CheckSessionStatusParams): OperationResult<AutentixSessionStatus, Error>
}
