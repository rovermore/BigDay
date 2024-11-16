package com.smallworldfs.moneytransferapp.data.account.beneficiary.repository.network

import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.BeneficiaryActivityListResponse
import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.BeneficiaryDeleteResponse
import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.BeneficiaryListResponse
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

class BeneficiaryNetworkDatasource(
    private val service: BeneficiaryService
) : NetworkDatasource() {

    fun deleteBeneficiary(
        userToken: String,
        userId: String,
        beneficiaryId: String
    ): OperationResult<BeneficiaryDeleteResponse, APIError> =
        executeCall(
            service.deleteBeneficiary(
                userToken,
                userId,
                beneficiaryId
            )
        )

    fun requestBeneficiaryActivity(
        userToken: String,
        userId: String,
        beneficiaryId: String
    ): OperationResult<BeneficiaryActivityListResponse, APIError> =
        executeCall(
            service.requestBeneficiaryActivityList(
                userToken,
                userId,
                beneficiaryId
            )
        )
    fun requestBeneficiaryList(filter: String, offset: String, limit: String, userId: String, userToken: String): OperationResult<BeneficiaryListResponse, APIError> =
        executeCall(
            service.requestBeneficiaryList(
                userToken,
                userId,
                filter,
                offset,
                limit
            )
        )
}
