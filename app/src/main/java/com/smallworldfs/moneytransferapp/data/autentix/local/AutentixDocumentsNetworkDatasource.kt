package com.smallworldfs.moneytransferapp.data.autentix.local

import com.smallworldfs.moneytransferapp.data.account.documents.repository.network.UserAuthenticationService
import com.smallworldfs.moneytransferapp.data.autentix.network.model.AutentixSessionStatusResponse
import com.smallworldfs.moneytransferapp.data.autentix.network.model.CreateAutentixSessionResponse
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

class AutentixDocumentsNetworkDatasource(private val service: UserAuthenticationService) : NetworkDatasource() {

    fun createAutentixSession(uuid: String, userToken: String, lang: String, documentType: String, faceCompare: String?): OperationResult<CreateAutentixSessionResponse, APIError> =
        executeCall(service.createAutentixSession(uuid, lang, userToken, documentType, faceCompare))

    fun checkAutentixSessionsStatus(uuid: String, externalId: String, lang: String, userToken: String): OperationResult<AutentixSessionStatusResponse, APIError> {
        return executeCall(
            service.checkAutentixSessionStatus(
                uuid,
                externalId,
                lang,
                userToken
            )
        )
    }
}
