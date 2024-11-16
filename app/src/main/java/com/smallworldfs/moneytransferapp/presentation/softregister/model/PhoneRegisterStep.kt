package com.smallworldfs.moneytransferapp.presentation.softregister.model

sealed class PhoneRegisterStep
object SendPhone : PhoneRegisterStep()
object VerifyCode : PhoneRegisterStep()
object VerificationCompleted : PhoneRegisterStep()
