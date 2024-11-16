package com.smallworldfs.moneytransferapp.data.mtn

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.mtn.model.MTNRequest
import com.smallworldfs.moneytransferapp.data.mtn.model.MtnStatusDTOMapper
import com.smallworldfs.moneytransferapp.data.mtn.network.TransactionTrackingNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.model.MtnStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.repository.MTNRepository
import javax.inject.Inject

class MTNRepositoryImpl @Inject constructor(
    private val transactionTrackingNetworkDatasource: TransactionTrackingNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper,
    private val mtnStatusDTOMapper: MtnStatusDTOMapper
) : MTNRepository {

    override fun getMtnStatus(mtn: String, country: String): OperationResult<MtnStatusDTO, Error> {
        return transactionTrackingNetworkDatasource.trackTransaction(
            MTNRequest(country, mtn)
        ).map {
            return mtnStatusDTOMapper.map(it)
        }.mapFailure {
            return Failure(apiErrorMapper.map(it))
        }
    }
}
