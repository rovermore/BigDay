package com.smallworldfs.moneytransferapp.data.offices.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class OfficesPoiResponse(
    var locations: List<Location>,
    var msg: String = STRING_EMPTY
)

data class Location(
    var address: String? = STRING_EMPTY,
    var city: String? = STRING_EMPTY,
    var cp: String? = STRING_EMPTY,
    var email: String? = STRING_EMPTY,
    var fax: String? = STRING_EMPTY,
    var festive: String? = STRING_EMPTY,
    var id: String? = STRING_EMPTY,
    var latitude: String? = STRING_EMPTY,
    var longitude: String? = STRING_EMPTY,
    var miles: String? = STRING_EMPTY,
    var name: String? = STRING_EMPTY,
    var office_code: String? = STRING_EMPTY,
    var phone: String? = STRING_EMPTY,
    var province: String? = STRING_EMPTY,
    var timetable1: String? = STRING_EMPTY,
    var timetable2: String? = STRING_EMPTY,
    var url: String? = STRING_EMPTY
)
