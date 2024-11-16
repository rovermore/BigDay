package com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.model

import com.smallworldfs.moneytransferapp.utils.KeyValueModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class BeneficiaryDTO(
    val id: String = STRING_EMPTY,
    val clientId: String = STRING_EMPTY,
    val clientRelationId: String = STRING_EMPTY,
    val alias: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    val surname: String = STRING_EMPTY,
    val email: String = STRING_EMPTY,
    val mobile: String = STRING_EMPTY,
    val city: String = STRING_EMPTY,
    val country: String = STRING_EMPTY,
    val address: String = STRING_EMPTY,
    val zip: String = STRING_EMPTY,
    val state: String = STRING_EMPTY,
    val deliveryMethod: KeyValueModel = KeyValueModel(),
    val payoutCountry: KeyValueModel = KeyValueModel(),
    val payoutCurrency: KeyValueModel = KeyValueModel(),
    val bankName: String = STRING_EMPTY,
    val bankAccountType: KeyValueModel = KeyValueModel(),
    val bankAccountNumber: String = STRING_EMPTY,
    val document: String = STRING_EMPTY,
    val numberDocument: String = STRING_EMPTY,
    val countryDocument: String = STRING_EMPTY,
    val currentRepresentativeCode: String = STRING_EMPTY,
    val currentLocationCode: String = STRING_EMPTY,
    val beneficiaryType: String = STRING_EMPTY,
    val isNew: Boolean = false,
    val nameOrNickName: String = STRING_EMPTY,
    val fullNameWithSurname: String = STRING_EMPTY
)
