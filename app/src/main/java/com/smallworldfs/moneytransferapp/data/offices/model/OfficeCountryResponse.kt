package com.smallworldfs.moneytransferapp.data.offices.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlin.collections.HashMap

data class OfficeCountryResponse(
    var msg: String = STRING_EMPTY,
    var countries: ArrayList<HashMap<String, String>> = arrayListOf()
)
