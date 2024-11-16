package com.smallworldfs.moneytransferapp.data.account.beneficiary.repository.mappers

import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.BeneficiaryActivityListResponse
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.model.BeneficiaryActivityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.model.TransactionInfoDTO
import javax.inject.Inject

class BeneficiaryActivityDTOMapper @Inject constructor() {

    fun map(response: BeneficiaryActivityListResponse): BeneficiaryActivityDTO {
        return BeneficiaryActivityDTO(
            TransactionInfoDTO(
                response.transactionsInfo.moneySent,
                response.transactionsInfo.moneyReceived,
                response.transactionsInfo.transactionsCount,
            ),
            response.transactions,
            response.cancellationMessage
        )
    }
}
