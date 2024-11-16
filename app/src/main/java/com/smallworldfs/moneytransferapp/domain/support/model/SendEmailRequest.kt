package com.smallworldfs.moneytransferapp.domain.support.model

import com.smallworldfs.moneytransferapp.utils.Constants

class SendEmailRequest(subject: String, body: String, to: String, userToken: String, userId: String) : HashMap<String, String>() {

    init {
        put("subject", subject)
        put("body", body)
        put("to", to)
        put("userToken", userToken)
        put("type", Constants.EMAIL.TYPE)
        put("userId", userId)
        put("key", Constants.EMAIL.EMAIL_KEY)
    }
}
