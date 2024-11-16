package com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel

sealed class ChangePasswordError
object NoConnection : ChangePasswordError()
object SessionExpired : ChangePasswordError()
data class PasswordNotChanged(val title: String, val message: String) : ChangePasswordError()
object ChangePasswordCommonError : ChangePasswordError()
