package com.smallworldfs.moneytransferapp.base.presentation.viewmodel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Base class to handle every use case.
 *
 * All the logic associated to data retrieving must be done inside an use case.
 */

/**
 * @param I Input. Must be the model object that the use case can use to make the request
 * @param O Output.Must be the model object that the use case must return
 */

abstract class BaseUseCase<I, O> {

    /**
     * Executes a function inside a background thread provided by dispatcher
     * @return the deferred object with the return value
     */
    suspend fun execute(input: I): O {
        return withContext(dispatcher) { useCaseFunction(input) }
    }

    /**
     * Function to implement by child classes to execute the code associated to data retrieving.
     * It will be executed on background thread
     */
    protected abstract suspend fun useCaseFunction(input: I): O

    /**
     * Dispatcher used for useCase execution
     */
    protected open val dispatcher: CoroutineDispatcher = Dispatchers.IO
}
