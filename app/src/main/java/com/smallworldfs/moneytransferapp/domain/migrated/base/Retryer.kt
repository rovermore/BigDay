package com.smallworldfs.moneytransferapp.domain.migrated.base

interface Retryer {
    fun <T, S> retry(params: S, block: (S) -> OperationResult<T, Error>, timeout: Long, numIntervals: Int): OperationResult<T, Error>
}
