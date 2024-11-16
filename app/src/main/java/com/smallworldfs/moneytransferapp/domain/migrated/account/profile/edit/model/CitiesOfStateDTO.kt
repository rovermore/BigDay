package com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model

import java.util.TreeMap

data class CitiesOfStateDTO(
    val data: ArrayList<TreeMap<String, String>> = arrayListOf()
)
