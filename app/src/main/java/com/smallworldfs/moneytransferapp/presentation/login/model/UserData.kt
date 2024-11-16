package com.smallworldfs.moneytransferapp.presentation.login.model

import com.smallworldfs.moneytransferapp.modules.login.domain.model.Gdpr
import com.smallworldfs.moneytransferapp.presentation.common.CountriesData

data class UserData(
    val id: String,
    val clientId: String,
    val name: String,
    val secondName: String,
    val surname: String,
    val secondSurname: String,
    val email: String,
    val country: CountriesData,
    val birthDate: String,
    val phone: String,
    val mobile: String,
    val address: String,
    val cp: String,
    val city: String,
    val status: String,
    val streetNumber: String,
    val buildingName: String,
    val appToken: String,
    val mobilePhoneCountry: String,
    val userToken: String,
    val kountsessid: String,
    val flinksState: String,
    val gdpr: Gdpr,
    val freshchatId: String,
    val createPassCode: Boolean = false
)
