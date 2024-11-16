package com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel

sealed class ChangePasswordLoaded
data class ChangePasswordFormLoaded(val changePasswordForm: ChangePasswordForm) : ChangePasswordLoaded()
object PasswordChanged : ChangePasswordLoaded()
