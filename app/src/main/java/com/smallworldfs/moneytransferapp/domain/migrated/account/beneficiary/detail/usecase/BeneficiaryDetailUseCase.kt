package com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.model.BeneficiaryActivityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.repository.BeneficiaryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class BeneficiaryDetailUseCase @Inject constructor(
    private val beneficiaryRepository: BeneficiaryRepository,
    private val userDataRepository: UserDataRepository,
) {

    fun deleteBeneficiary(beneficiaryId: String): OperationResult<Boolean, Error> {
        return userDataRepository.getLoggedUser()
            .mapFailure {
                return Failure(Error.UncompletedOperation("Could not retrieve user"))
            }.map { user ->
                return beneficiaryRepository.deleteBeneficiary(
                    user.userToken,
                    user.id,
                    beneficiaryId
                )
            }
    }

    fun getBeneficiaryActivity(beneficiaryId: String): OperationResult<BeneficiaryActivityDTO, Error> {
        return userDataRepository.getLoggedUser()
            .mapFailure {
                return Failure(Error.UncompletedOperation("Could not retrieve user"))
            }.map { user ->
                return beneficiaryRepository.getBeneficiaryActivity(
                    user.userToken,
                    user.id,
                    beneficiaryId
                )
            }
    }
}
