package com.smallworldfs.moneytransferapp.data.account.beneficiary.model

import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.model.BeneficiaryDTO
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Beneficiary
import com.smallworldfs.moneytransferapp.utils.KeyValueModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class BeneficiaryDTOMapper @Inject constructor() {

    fun map(beneficiaries: List<Beneficiary>): List<BeneficiaryDTO> =
        beneficiaries.map {
            map(it)
        }

    fun map(beneficiary: Beneficiary): BeneficiaryDTO =
        with(beneficiary) {
            BeneficiaryDTO(
                id ?: STRING_EMPTY,
                clientId ?: STRING_EMPTY,
                clientRelationId ?: STRING_EMPTY,
                alias ?: STRING_EMPTY,
                name ?: STRING_EMPTY,
                surname ?: STRING_EMPTY,
                email ?: STRING_EMPTY,
                mobile ?: STRING_EMPTY,
                city ?: STRING_EMPTY,
                country ?: STRING_EMPTY,
                address ?: STRING_EMPTY,
                zip ?: STRING_EMPTY,
                state ?: STRING_EMPTY,
                KeyValueModel(deliveryMethod.firstEntry()?.key ?: STRING_EMPTY, deliveryMethod.firstEntry()?.value ?: STRING_EMPTY),
                KeyValueModel(payoutCountry.firstEntry()?.key ?: STRING_EMPTY, payoutCountry.firstEntry()?.value ?: STRING_EMPTY),
                KeyValueModel(payoutCurrency.firstEntry()?.key ?: STRING_EMPTY, payoutCurrency.firstEntry()?.value ?: STRING_EMPTY),
                bankName ?: STRING_EMPTY,
                KeyValueModel(bankAccountType.firstEntry()?.key ?: STRING_EMPTY, bankAccountType.firstEntry()?.value ?: STRING_EMPTY),
                bankAccountNumber ?: STRING_EMPTY,
                document ?: STRING_EMPTY,
                numberDocument ?: STRING_EMPTY,
                countryDocument ?: STRING_EMPTY,
                currentRepresentativeCode ?: STRING_EMPTY,
                currentLocationCode ?: STRING_EMPTY,
                beneficiaryType ?: STRING_EMPTY,
                isNew == 1,
                nameOrNickName ?: STRING_EMPTY,
                fullNameWithSurname ?: STRING_EMPTY
            )
        }
}
