package com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.model.BeneficiaryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.repository.BeneficiaryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.model.DeliveryMethodDTO
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.repository.CalculatorRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class BeneficiaryListUseCase @Inject constructor(
    private val beneficiaryRepository: BeneficiaryRepository,
    private val calculatorRepository: CalculatorRepository,
    private val userDataRepository: UserDataRepository,
) {

    fun getBeneficiaryList(filter: String, offset: String, limit: String): OperationResult<List<BeneficiaryDTO>, Error> =
        userDataRepository.getLoggedUser()
            .map { user ->
                return beneficiaryRepository.requestBeneficiaryList(
                    filter,
                    offset,
                    limit,
                    user.id,
                    user.userToken,
                )
            }

    fun getDeliveryMethods(): OperationResult<List<DeliveryMethodDTO>, Error> =
        calculatorRepository.getDeliveryMethods()
}
