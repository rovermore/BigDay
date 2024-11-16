package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model

import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.model.BeneficiaryDTO
import javax.inject.Inject

class BeneficiaryUIModelMapper @Inject constructor() {

    fun map(beneficiaries: List<BeneficiaryDTO>): List<BeneficiaryUIModel> =
        beneficiaries.map {
            map(it)
        }

    fun map(beneficiary: BeneficiaryDTO): BeneficiaryUIModel =
        with(beneficiary) {
            BeneficiaryUIModel(
                id,
                clientId,
                clientRelationId,
                alias,
                name,
                surname,
                email,
                mobile,
                city,
                country,
                address,
                zip,
                state,
                DeliveryMethodUIModel(deliveryMethod.key, deliveryMethod.value),
                PayoutCountryUIModel(payoutCountry.key, payoutCountry.value),
                payoutCurrency.value,
                bankName,
                bankAccountType.value,
                bankAccountNumber,
                document,
                numberDocument,
                countryDocument,
                currentRepresentativeCode,
                currentLocationCode,
                beneficiaryType,
                isNew,
                nameOrNickName,
                fullNameWithSurname
            )
        }
}
