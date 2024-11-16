package com.smallworldfs.moneytransferapp.data.account.profile.model

import com.smallworldfs.moneytransferapp.domain.model.FormModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class EditProfileFormResponse(
    var msg: String = STRING_EMPTY,
    var form: FormModel = FormModel()
)
