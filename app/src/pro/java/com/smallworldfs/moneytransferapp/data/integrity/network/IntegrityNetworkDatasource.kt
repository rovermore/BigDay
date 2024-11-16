package com.smallworldfs.moneytransferapp.data.integrity.network

import com.google.android.play.core.integrity.IntegrityManager
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.integrity.model.IntegrityDataResponse
import com.smallworldfs.moneytransferapp.data.integrity.model.IntegrityNonceRequest
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class IntegrityNetworkDatasource @Inject constructor(
    private val integrityService: IntegrityService,
    private val integrityManager: IntegrityManager
) : NetworkDatasource() {

    private fun createIntegrityToken(nonce: String): OperationResult<String, APIError> {
        val deferred = CompletableDeferred<String>()
        var integrityException: Exception? = null

        integrityManager.requestIntegrityToken(
            IntegrityTokenRequest.builder()
                .setNonce(nonce)
                .build()
        ).addOnSuccessListener {
            deferred.complete(it.token())
        }.addOnFailureListener {
            integrityException = it
            deferred.complete(STRING_EMPTY)
        }

        val result = runBlocking {
            val result = deferred.await()
            if (result.isNotEmpty()) {
                Success(result)
            } else {
                Failure(
                    mapAPIError(
                        mapNetworkExceptions(
                            400,
                            integrityException?.localizedMessage
                        )
                    )
                )
            }
        }
        return result
    }

    fun getIntegrity(operation: String): OperationResult<IntegrityDataResponse, APIError> {
        return executeCall(integrityService.getSecurityNonce(IntegrityNonceRequest(operation)))
            .peek { dataResponse ->
                createIntegrityToken(dataResponse.data.nonce).map { token ->
                    return Success(dataResponse.copy(data = dataResponse.data, signature = token))
                }.mapFailure {
                    return Failure(it)
                }
            }
    }
}
