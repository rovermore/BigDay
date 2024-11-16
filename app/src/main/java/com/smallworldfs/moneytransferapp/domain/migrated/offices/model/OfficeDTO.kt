package com.smallworldfs.moneytransferapp.domain.migrated.offices.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class OfficeDTO(
    val address: String = STRING_EMPTY,
    val city: String = STRING_EMPTY,
    val country: String = STRING_EMPTY,
    val cp: String = STRING_EMPTY,
    val email: String = STRING_EMPTY,
    val fax: String = STRING_EMPTY,
    val festive: String = STRING_EMPTY,
    var id: String = STRING_EMPTY,
    val latitude: String = STRING_EMPTY,
    val longitude: String = STRING_EMPTY,
    var miles: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    var officeCode: String = STRING_EMPTY,
    val phone: String = STRING_EMPTY,
    val province: String = STRING_EMPTY,
    val timetable1: String = STRING_EMPTY,
    val timetable2: String = STRING_EMPTY,
    var url: String = STRING_EMPTY
)
