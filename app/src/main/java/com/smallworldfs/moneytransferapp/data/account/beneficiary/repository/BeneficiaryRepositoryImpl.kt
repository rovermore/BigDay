package com.smallworldfs.moneytransferapp.data.account.beneficiary.repository

import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.BeneficiaryDTOMapper
import com.smallworldfs.moneytransferapp.data.account.beneficiary.repository.mappers.BeneficiaryActivityDTOMapper
import com.smallworldfs.moneytransferapp.data.account.beneficiary.repository.network.BeneficiaryNetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.model.BeneficiaryActivityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.model.BeneficiaryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.repository.BeneficiaryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import javax.inject.Inject

class BeneficiaryRepositoryImpl @Inject constructor(
    private val beneficiaryNetworkDatasource: BeneficiaryNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper,
    private val beneficiaryActivityDTOMapper: BeneficiaryActivityDTOMapper,
    private val beneficiaryDTOMapper: BeneficiaryDTOMapper,
) : BeneficiaryRepository {

    override fun deleteBeneficiary(
        userToken: String,
        userId: String,
        beneficiaryId: String
    ): OperationResult<Boolean, Error> {
        return beneficiaryNetworkDatasource.deleteBeneficiary(
            userToken,
            userId,
            beneficiaryId
        )
            .map {
                return Success(true)
            }
            .mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }

    override fun getBeneficiaryActivity(
        userToken: String,
        userId: String,
        beneficiaryId: String
    ): OperationResult<BeneficiaryActivityDTO, Error> {
        return beneficiaryNetworkDatasource.requestBeneficiaryActivity(
            userToken,
            userId,
            beneficiaryId
        ).map {
            return Success(beneficiaryActivityDTOMapper.map(it))
        }.mapFailure {
            return Failure(apiErrorMapper.map(it))
        }
    }

    override fun requestBeneficiaryList(filter: String, offset: String, limit: String, userId: String, userToken: String): OperationResult<List<BeneficiaryDTO>, Error> =
        beneficiaryNetworkDatasource.requestBeneficiaryList(filter, offset, limit, userId, userToken)
            .map {
                beneficiaryDTOMapper.map(it.beneficiaries)
            }.mapFailure {
                apiErrorMapper.map(it)
            }
}
