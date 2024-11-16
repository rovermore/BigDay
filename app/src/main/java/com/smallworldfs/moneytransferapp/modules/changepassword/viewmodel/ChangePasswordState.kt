package com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel

sealed class ChangePasswordState
object Loading : ChangePasswordState()
data class Loaded(val loaded: ChangePasswordLoaded) : ChangePasswordState()
data class Error(val error: ChangePasswordError) : ChangePasswordState()
