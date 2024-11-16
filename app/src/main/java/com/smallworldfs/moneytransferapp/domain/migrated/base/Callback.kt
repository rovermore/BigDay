package com.smallworldfs.moneytransferapp.domain.migrated.base

interface Callback<T, Error> {
    fun onSuccess(result: T)
    fun onFailure(e: Error)
}
