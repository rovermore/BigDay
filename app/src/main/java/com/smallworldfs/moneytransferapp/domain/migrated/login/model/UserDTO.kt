package com.smallworldfs.moneytransferapp.domain.migrated.login.model

import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.modules.login.domain.model.Gdpr
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class UserDTO(
    val id: String = STRING_EMPTY,
    val clientId: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    val secondName: String = STRING_EMPTY,
    val surname: String = STRING_EMPTY,
    val secondSurname: String = STRING_EMPTY,
    val email: String = STRING_EMPTY,
    val country: CountriesDTO = CountriesDTO(),
    val destinationCountry: CountryDTO = CountryDTO(),
    val birthDate: String = STRING_EMPTY,
    val phone: String = STRING_EMPTY,
    val mobile: String = STRING_EMPTY,
    val address: String = STRING_EMPTY,
    val cp: String = STRING_EMPTY,
    val city: String = STRING_EMPTY,
    val status: String = STRING_EMPTY,
    val streetNumber: String = STRING_EMPTY,
    val buildingName: String = STRING_EMPTY,
    val appToken: String = STRING_EMPTY,
    val mobilePhoneCountry: String = STRING_EMPTY,
    val userToken: String = STRING_EMPTY,
    val kountsessid: String = STRING_EMPTY,
    val flinksState: String = STRING_EMPTY,
    val gdpr: Gdpr = Gdpr(),
    var freshchatId: String = STRING_EMPTY,
    val finishedTransactions: String = STRING_EMPTY,
    val uuid: String = STRING_EMPTY,
    val emailValidated: Boolean = false,
    var showEmailValidated: Boolean = false,
    val receiveNewsletters: Boolean = false,
    val receiveStatusTrans: Boolean = false,
    val authenticated: Boolean = false
) {
    companion object {
        const val LIMITED = "LIMITED"
        const val APPROVED = "APPROVED"
        const val PDT_SMS = "PDT_SMS"
        const val UNDER_REVIEW = "UNDER_REVIEW"
        const val PDT_MAIL = "PDT_MAIL"
        const val PDT_PROFILE = "PDT_PROFILE"
    }

    fun isLimited() = status == LIMITED
    fun isApproved() = status == APPROVED
    fun isFullyRegistered() = status == APPROVED || status == PDT_MAIL || status == UNDER_REVIEW
    fun hasPendingPhoneValidation() = status == PDT_SMS
    fun hasPendingProfileValidation() = status == PDT_PROFILE

    fun getCountry() = country.countries.firstOrNull()?.iso3 ?: ""
}
