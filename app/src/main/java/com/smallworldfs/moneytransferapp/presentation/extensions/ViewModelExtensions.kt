package com.smallworldfs.moneytransferapp.presentation.extensions

import androidx.lifecycle.ViewModel
import com.smallworldfs.moneytransferapp.domain.migrated.base.Callback
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> ViewModel.awaitCallback(block: (Callback<T, Error>) -> Unit): T =
    suspendCancellableCoroutine { cont ->
        block(object : Callback<T, Error> {
            override fun onSuccess(result: T) {
                cont.resume(result)
            }

            override fun onFailure(e: Error) {
                cont.resumeWithException(Exception(e.message))
            }
        })
    }
