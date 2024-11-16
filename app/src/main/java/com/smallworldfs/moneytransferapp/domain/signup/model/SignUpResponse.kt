package com.smallworldfs.moneytransferapp.domain.signup.model

import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form

data class SignUpResponse(var msg: String = "", var form: Form? = null)
