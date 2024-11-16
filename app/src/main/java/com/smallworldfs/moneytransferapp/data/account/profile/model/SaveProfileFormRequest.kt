package com.smallworldfs.moneytransferapp.data.account.profile.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

class SaveProfileFormRequest(userToken: String = STRING_EMPTY, userId: String = STRING_EMPTY) : HashMap<String, String>() {
    init {
        put("userToken", userToken)
        put("userId", userId)
    }
}
