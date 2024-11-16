package com.smallworldfs.moneytransferapp.utils

import java.io.Serializable

data class RefApiModel(
    val url: String = STRING_EMPTY,
    val params: ArrayList<String> = arrayListOf()
) : Serializable
