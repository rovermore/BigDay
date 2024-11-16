package com.smallworldfs.moneytransferapp.presentation.login.model

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.modules.login.domain.model.Gdpr
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountriesData
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserUIModel(
    val id: String = STRING_EMPTY,
    val clientId: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    val secondName: String = STRING_EMPTY,
    val surname: String = STRING_EMPTY,
    val secondSurname: String = STRING_EMPTY,
    val email: String = STRING_EMPTY,
    val country: CountriesData = CountriesData(),
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
    val freshchatId: String = STRING_EMPTY,
    val createPassCode: Boolean = false
) : Parcelable {
    fun isLimitedUser() = status == Constants.UserType.LIMITED
    fun isApprovedUser() = status == Constants.UserType.APPROVED || status == Constants.UserType.UNDER_REVIEW
}
