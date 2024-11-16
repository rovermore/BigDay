package com.smallworldfs.moneytransferapp.domain.token.model

class AppTokenRequest(appToken: String = "", country: String) : HashMap<String, String>() {

    init {
        put("appToken", appToken)
        put("country", country)
    }
}
