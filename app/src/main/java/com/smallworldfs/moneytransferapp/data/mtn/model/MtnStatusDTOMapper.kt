package com.smallworldfs.moneytransferapp.data.mtn.model

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.model.MtnStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.model.StatusDTO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class MtnStatusDTOMapper @Inject constructor() {

    fun map(transactionResponse: TransactionTrackingResponse): OperationResult<MtnStatusDTO, Error> {
        val statusList = mutableListOf<StatusDTO>()
        transactionResponse.statuses.forEach {
            statusList.add(StatusDTO(it.id, it.title, it.status))
        }
        return if (!transactionResponse.status.equals("NOT_FOUND"))
            Success(
                MtnStatusDTO(
                    transactionResponse.status,
                    statusList,
                    transactionResponse.country ?: STRING_EMPTY,
                    transactionResponse.mtn ?: STRING_EMPTY,
                ),
            )
        else Failure(Error.OperationCompletedWithError(transactionResponse.statusMsg))
    }
}
