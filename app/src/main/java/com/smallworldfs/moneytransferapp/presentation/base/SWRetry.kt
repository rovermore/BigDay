package com.smallworldfs.moneytransferapp.presentation.base

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Retryer
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import javax.inject.Inject
import kotlin.math.pow

class SWRetry @Inject constructor(private val scheduler: Scheduler) : Retryer {

    override fun <T, S> retry(params: S, block: (S) -> OperationResult<T, Error>, timeout: Long, numIntervals: Int): OperationResult<T, Error> {
        val intervals = calculateIntervals(timeout, numIntervals)
        var tries = 0
        var result: OperationResult<T, Error>
        do {
            result = scheduler.schedule(intervals[tries], params, block)
            tries += 1
        } while (result !is Success && tries < intervals.size)
        return result
    }

    private fun calculateIntervals(max: Long, numIntervals: Int): List<Long> {
        val intervals = mutableListOf<Double>()
        val base = 2.0
        val n: Double = base.pow(numIntervals) - 1
        val d = base - 1
        val t = max / (n / d)
        for (x in 0 until numIntervals) {
            val interval: Double = t * base.pow(x)
            intervals.add(interval)
        }
        return intervals.map { it.toLong() }
    }
}
