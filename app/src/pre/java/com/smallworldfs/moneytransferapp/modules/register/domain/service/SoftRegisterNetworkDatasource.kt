package com.smallworldfs.moneytransferapp.modules.register.domain.service

import com.google.android.play.core.integrity.IntegrityManager
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.Integrity
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestInfo
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.integrity.model.IntegrityNonceRequest
import com.smallworldfs.moneytransferapp.data.oauth.model.ResponseOAuthTokenDataModel
import retrofit2.Response
import rx.Single

class SoftRegisterNetworkDatasource(
    private val service: SoftRegisterService,
    private val integrityManager: IntegrityManager
) : NetworkDatasource() {

    fun refreshAccessToken(OAuthRequest: RequestOAuthTokenDataModel): Single<Response<ResponseOAuthTokenDataModel>> =
        executeCall(service.refreshAccessToken(OAuthRequest))

    private fun createIntegrityToken(nonce: String): Single<Integrity> {
        return Single.create { emitter ->
            integrityManager.requestIntegrityToken(
                IntegrityTokenRequest.builder()
                    .setCloudProjectNumber(244208716140L)
                    .setNonce(nonce)
                    .build(),
            ).addOnSuccessListener {
                emitter.onSuccess(Integrity(nonce, RequestInfo(it.token())))
            }.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }

    fun getIntegrity(operation: String): Single<Integrity> {
        return executeCall(service.getSecurityNonce(IntegrityNonceRequest(operation))).flatMap {
            createIntegrityToken(it.body()?.data?.nonce ?: "")
        }
    }
}
