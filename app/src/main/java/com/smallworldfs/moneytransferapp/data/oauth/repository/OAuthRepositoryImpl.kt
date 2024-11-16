package com.smallworldfs.moneytransferapp.data.oauth.repository

import com.smallworldfs.moneytransferapp.data.auth.oauth.model.Integrity
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestInfo
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.oauth.repository.local.OAuthLocal
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.oauth.repository.OAuthRepository
import com.smallworldfs.moneytransferapp.domain.migrated.operations.model.IntegrityDTO
import com.smallworldfs.moneytransferapp.modules.oauth.domain.service.OAuthNetworkDatasource
import javax.inject.Inject

class OAuthRepositoryImpl @Inject constructor(
    private val oAuthNetwork: OAuthNetworkDatasource,
    private val oAuthLocal: OAuthLocal,
    private val apiErrorMapper: APIErrorMapper
) : OAuthRepository {

    override fun requestOAuthTokenAsync(integrityDTO: IntegrityDTO): OperationResult<String, Error> {
        return if (oauthTokenExists()) {
            Success(oAuthLocal.getPersistedOAuthToken())
        } else {
            refreshOAuthTokenAsync(integrityDTO)
        }
    }

    override fun refreshOAuthTokenAsync(integrityDTO: IntegrityDTO): OperationResult<String, Error> {
        return oAuthNetwork.getSyncAccessToken(
            RequestOAuthTokenDataModel(
                Integrity(
                    integrityDTO.nonce, RequestInfo(integrityDTO.requestInfo.signature),
                ),
            ),
        ).map {
            oAuthLocal.persistOAuthToken(it.getOAuthToken())
            it.accessToken
        }.mapFailure {
            apiErrorMapper.map(it)
        }
    }

    private fun oauthTokenExists() = oAuthLocal.getPersistedOAuthToken().isNotBlank() && oAuthLocal.getPersistedOAuthToken().isNotEmpty()
}
