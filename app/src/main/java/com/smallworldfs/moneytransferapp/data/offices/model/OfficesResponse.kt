package com.smallworldfs.moneytransferapp.data.offices.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class OfficesResponse(
    var branches: List<Branches> = listOf(),
    var msg: String = STRING_EMPTY
)

data class Branches(
    var address: String? = STRING_EMPTY,
    var city: String? = STRING_EMPTY,
    var country: String? = STRING_EMPTY,
    var cp: String? = STRING_EMPTY,
    var email: String? = STRING_EMPTY,
    var fax: String? = STRING_EMPTY,
    var festive: String? = STRING_EMPTY,
    var latitude: String? = STRING_EMPTY,
    var longitude: String? = STRING_EMPTY,
    var name: String? = STRING_EMPTY,
    var phone: String? = STRING_EMPTY,
    var province: String? = STRING_EMPTY,
    var timetable1: String? = STRING_EMPTY,
    var timetable2: String? = STRING_EMPTY
)
