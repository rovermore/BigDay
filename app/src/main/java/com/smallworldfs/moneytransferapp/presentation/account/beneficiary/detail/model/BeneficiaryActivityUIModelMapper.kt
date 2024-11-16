package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.model

import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.model.BeneficiaryActivityDTO
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Beneficiary
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel
import javax.inject.Inject

class BeneficiaryActivityUIModelMapper @Inject constructor() {

    fun map(beneficiaryActivityDTO: BeneficiaryActivityDTO): BeneficiaryActivityUIModel {
        return BeneficiaryActivityUIModel(
            TransactionInfoUIModel(
                beneficiaryActivityDTO.transactionInfoDTO.moneySent,
                beneficiaryActivityDTO.transactionInfoDTO.moneyReceived,
                beneficiaryActivityDTO.transactionInfoDTO.transactionsCount,
            ),
            beneficiaryActivityDTO.transactions,
            beneficiaryActivityDTO.cancellationMessage,
        )
    }

    fun map(uiModel: BeneficiaryUIModel) = Beneficiary(uiModel)
}
