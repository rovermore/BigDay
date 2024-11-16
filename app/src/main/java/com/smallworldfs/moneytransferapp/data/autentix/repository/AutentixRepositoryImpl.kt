package com.smallworldfs.moneytransferapp.data.autentix.repository

import com.smallworldfs.moneytransferapp.data.autentix.local.AutentixDocumentsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.autentix.model.AutentixSessionStatus
import com.smallworldfs.moneytransferapp.data.autentix.model.CheckSessionStatusParams
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.AutentixSessionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.autentix.repository.AutentixRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import javax.inject.Inject

class AutentixRepositoryImpl @Inject constructor(
    private val autentixDocumentsNetworkDatasource: AutentixDocumentsNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper
) : AutentixRepository {

    private companion object {
        const val STATUS_FINISHED = "FINISHED"
        const val PENDING = "PENDING"
    }

    override fun getAutentixSessionURL(uuid: String, userToken: String, lang: String, faceCompare: Boolean, documentType: String): OperationResult<AutentixSessionDTO, Error> {
        return autentixDocumentsNetworkDatasource.createAutentixSession(
            uuid,
            userToken,
            lang,
            documentType,
            if (faceCompare) "faceCompare" else null,
        ).mapFailure {
            apiErrorMapper.map(it)
        }.map {
            AutentixSessionDTO(it.url, it.externalId, it.timeout)
        }
    }

    override fun checkAutentixSessionStatus(params: CheckSessionStatusParams): OperationResult<AutentixSessionStatus, Error> {
        return autentixDocumentsNetworkDatasource.checkAutentixSessionsStatus(
            params.uuid,
            params.externalId,
            params.lang,
            params.userToken
        ).mapFailure {
            return Failure(Error.ValidationPending())
        }.map {
            return when (it.status) {
                STATUS_FINISHED -> Success(AutentixSessionStatus.FINISHED)
                else -> Failure(Error.ValidationPending())
            }
        }
    }
}
