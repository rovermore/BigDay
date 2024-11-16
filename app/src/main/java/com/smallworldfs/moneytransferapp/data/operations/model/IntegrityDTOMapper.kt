package com.smallworldfs.moneytransferapp.data.operations.model

import com.smallworldfs.moneytransferapp.data.integrity.model.IntegrityDataResponse
import com.smallworldfs.moneytransferapp.domain.migrated.operations.model.IntegrityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.operations.model.RequestInfoDTO
import javax.inject.Inject

class IntegrityDTOMapper @Inject constructor() {
    fun map(integrityDataResponse: IntegrityDataResponse): IntegrityDTO =
        IntegrityDTO(
            nonce = integrityDataResponse.data.nonce,
            requestInfo = RequestInfoDTO(signature = integrityDataResponse.signature)
        )
}
