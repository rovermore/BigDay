package com.smallworldfs.moneytransferapp.utils

import java.io.Serializable

data class KeyValueModel(
    val key: String = STRING_EMPTY,
    val value: String = STRING_EMPTY
) : Serializable
