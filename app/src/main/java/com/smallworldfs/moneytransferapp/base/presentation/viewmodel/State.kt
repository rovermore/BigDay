package com.smallworldfs.moneytransferapp.base.presentation.viewmodel

sealed class State {
    data class Loading<T>(val data: T) : State()
    data class Success<T>(val data: T? = null) : State()
    data class Error(val data: DataError) : State()
}
