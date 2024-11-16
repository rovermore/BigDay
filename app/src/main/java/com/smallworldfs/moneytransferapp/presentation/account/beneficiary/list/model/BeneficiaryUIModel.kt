package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class BeneficiaryUIModel(
    var id: String = STRING_EMPTY,
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
    val deliveryMethod: DeliveryMethodUIModel = DeliveryMethodUIModel(),
    val payoutCountry: PayoutCountryUIModel = PayoutCountryUIModel(),
    val payoutCurrency: String = STRING_EMPTY,
    val bankName: String = STRING_EMPTY,
    val bankAccountType: String = STRING_EMPTY,
    val bankAccountNumber: String = STRING_EMPTY,
    val document: String = STRING_EMPTY,
    val numberDocument: String = STRING_EMPTY,
    val countryDocument: String = STRING_EMPTY,
    val currentRepresentativeCode: String = STRING_EMPTY,
    val currentLocationCode: String = STRING_EMPTY,
    val beneficiaryType: String = STRING_EMPTY,
    var isNew: Boolean = false,
    val nameOrNickName: String = STRING_EMPTY,
    val fullNameWithSurname: String = STRING_EMPTY
) : Parcelable, Serializable

@Parcelize
data class PayoutCountryUIModel(
    val iso3: String = STRING_EMPTY,
    val name: String = STRING_EMPTY
) : Parcelable, Serializable

@Parcelize
data class DeliveryMethodUIModel(
    val type: String = STRING_EMPTY,
    val name: String = STRING_EMPTY
) : Parcelable, Serializable
