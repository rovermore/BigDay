package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.account.profile.model.EditProfileFormResponse
import com.smallworldfs.moneytransferapp.domain.model.FormModel

object EditProfileFormResponseMock {

    val editProfileFormResponse = EditProfileFormResponse(
        "msg",
        FormModel()
    )
}
