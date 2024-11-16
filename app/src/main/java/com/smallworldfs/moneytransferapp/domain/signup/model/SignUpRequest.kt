package com.smallworldfs.moneytransferapp.domain.signup.model

class SignUpRequest(var country: String) : HashMap<String, String>() {

    init {
        put("country", country)
        put("soft", "1")
    }

    constructor(country: String, userToken: String, userId: String) : this(country) {
        put("userToken", userToken)
        put("userId", userId)
        put("soft", "0")
    }
}
