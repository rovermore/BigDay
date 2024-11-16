package com.smallworldfs.moneytransferapp.presentation.account.profile.show.model

import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryData
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class ProfileUIModel(
    val id: String = STRING_EMPTY,
    val clientId: String = STRING_EMPTY,
    val country: MutableList<CountryData> = mutableListOf(),
    val name: String = STRING_EMPTY,
    val secondName: String = STRING_EMPTY,
    val surname: String = STRING_EMPTY,
    val secondSurname: String = STRING_EMPTY,
    val phone: String = STRING_EMPTY,
    val mobile: String = STRING_EMPTY,
    val birthDate: String = STRING_EMPTY,
    val cp: String = STRING_EMPTY,
    val streetNumber: String = STRING_EMPTY,
    val buildingName: String = STRING_EMPTY,
    val address: String = STRING_EMPTY,
    val city: String = STRING_EMPTY,
    val receiveNewsletters: Boolean = false,
    val receiveStatusTrans: Boolean = false,
)
