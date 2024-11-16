package com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.repository

import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.model.BeneficiaryActivityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.model.BeneficiaryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

interface BeneficiaryRepository {

    fun deleteBeneficiary(
        userToken: String,
        userId: String,
        beneficiaryId: String
    ): OperationResult<Boolean, Error>

    fun getBeneficiaryActivity(
        userToken: String,
        userId: String,
        beneficiaryId: String
    ): OperationResult<BeneficiaryActivityDTO, Error>

    fun requestBeneficiaryList(filter: String, offset: String, limit: String, userId: String, userToken: String): OperationResult<List<BeneficiaryDTO>, Error>
}
