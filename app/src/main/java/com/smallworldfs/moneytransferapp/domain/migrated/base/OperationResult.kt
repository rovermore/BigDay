package com.smallworldfs.moneytransferapp.domain.migrated.base

sealed class OperationResult<out T, out E>

data class Success<out T>(val value: T) : OperationResult<T, Nothing>()
data class Failure<out E>(val reason: E) : OperationResult<Nothing, E>()

inline fun <T> resultFrom(block: () -> T): OperationResult<T, Exception> =
    try {
        Success(block())
    } catch (x: Exception) {
        Failure(x)
    }

inline fun <T, U, E> OperationResult<T, E>.map(f: (T) -> U): OperationResult<U, E> =
    flatMap { value -> Success(f(value)) }

inline fun <T, Tʹ, E> OperationResult<T, E>.flatMap(f: (T) -> OperationResult<Tʹ, E>): OperationResult<Tʹ, E> =
    when (this) {
        is Success<T> -> f(value)
        is Failure<E> -> this
    }

inline fun <T, E, Eʹ> OperationResult<T, E>.flatMapFailure(f: (E) -> OperationResult<T, Eʹ>): OperationResult<T, Eʹ> = when (this) {
    is Success<T> -> this
    is Failure<E> -> f(reason)
}

inline fun <T, E, Eʹ> OperationResult<T, E>.mapFailure(f: (E) -> Eʹ): OperationResult<T, Eʹ> =
    flatMapFailure { reason -> Failure(f(reason)) }

fun <T> OperationResult<T, T>.get() = when (this) {
    is Success<T> -> value
    is Failure<T> -> reason
}

inline fun <T, E> OperationResult<T, E>.onFailure(block: (Failure<E>) -> Nothing): T = when (this) {
    is Success<T> -> value
    is Failure<E> -> block(this)
}

inline fun <S, T : S, U : S, E> OperationResult<T, E>.recover(errorToValue: (E) -> U): S =
    mapFailure(errorToValue).get()

inline fun <T, E> OperationResult<T, E>.peek(f: (T) -> Unit) =
    apply { if (this is Success<T>) f(value) }

inline fun <T, E> OperationResult<T, E>.peekFailure(f: (E) -> Unit) =
    apply { if (this is Failure<E>) f(reason) }

inline fun <T, E> OperationResult<T, E>.then(f: () -> Unit) =
    apply { f() }

inline fun <T, E> List<OperationResult<T, E>>.zipSuccess(f: () -> Unit) =
    apply { if (this.filterIsInstance<Failure<E>>().isEmpty()) f() }

inline fun <T, E> List<OperationResult<T, E>>.zipFailure(f: (E) -> Unit) =
    apply {
        val errors = this.filterIsInstance<Failure<E>>()
        if (errors.isNotEmpty()) f(errors.first().reason)
    }

fun <T, E> List<OperationResult<T, E>>.concat(): OperationResult<List<T>, E> {
    apply {
        val errors = this.filterIsInstance<Failure<E>>()
        if (errors.isNotEmpty()) {
            return errors.first()
        } else {
            with((this as List<Success<T>>)) {
                return Success(this.map { it.value })
            }
        }
    }
}

inline fun <T, E> List<OperationResult<T, E>>.then(f: () -> Unit) =
    apply { f() }
