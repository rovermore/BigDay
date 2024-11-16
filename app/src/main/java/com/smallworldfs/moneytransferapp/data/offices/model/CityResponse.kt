package com.smallworldfs.moneytransferapp.data.offices.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlin.collections.HashMap

data class CityResponse(
    var msg: String = STRING_EMPTY,
    var cities: ArrayList<HashMap<String, String>> = arrayListOf()
)
