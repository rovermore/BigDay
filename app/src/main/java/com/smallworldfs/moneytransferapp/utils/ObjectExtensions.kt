package com.smallworldfs.moneytransferapp.utils

import com.google.gson.GsonBuilder
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success

fun Any?.toJSONString() =
    try {
        this ?: kotlin.run { Failure(Error.UnsupportedOperation("Data can't be null")) }
        val result = GsonBuilder().create().toJson(
            this,
            this!!.javaClass,
        )
        result?.let {
            Success(result)
        } ?: Failure(Error.UncompletedOperation("Data couldn't be read"))
    } catch (e: java.lang.Exception) {
        Failure(Error.UncompletedOperation("Data couldn't be read"))
    }
