package com.smallworldfs.moneytransferapp.presentation.base

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Scheduler @Inject constructor() {

    fun <T, S> schedule(timeInMillis: Long, params: S, task: (S) -> OperationResult<T, Error>): OperationResult<T, Error> {
        val deferred = CompletableDeferred<OperationResult<T, Error>>()

        val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

        backgroundExecutor.schedule({
            task.invoke(params)
                .map {
                    deferred.complete(Success(it))
                }.mapFailure {
                    deferred.complete(Failure(it))
                }
            backgroundExecutor.shutdown()
        }, timeInMillis, TimeUnit.MILLISECONDS)

        return runBlocking {
            deferred.await()
        }
    }
}
