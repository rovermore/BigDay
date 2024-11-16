package com.smallworldfs.moneytransferapp.data.account.profile.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.util.TreeMap

data class CitiesOfStateResponse(
    var msg: String = STRING_EMPTY,
    var data: ArrayList<TreeMap<String, String>> = arrayListOf()
)
