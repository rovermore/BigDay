package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.operations.model.IntegrityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.operations.model.RequestInfoDTO

object IntegrityDTOMock {

    private val requestInfoDTO = RequestInfoDTO(signature = "eyJhbGciOiJBMjU2S1ciLCJlbmMiOiJBMjU2R0NNIn0.-gJKpdvTcWNrqwx-5wfNOdmTDdAeH9rhwygZL-_jc0jQXaVAc0wuVQ.FlRsQp5QeQ2eYfoJ.tyExMYUR5BOyoB")

    val integrityDTO = IntegrityDTO(
        nonce = "02e41cceec437c211178b7a030c57179",
        requestInfo = requestInfoDTO
    )
}
