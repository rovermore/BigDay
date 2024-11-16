package com.smallworldfs.moneytransferapp.domain.support.model

class ContactSupportInfoRequest(country: String) : HashMap<String, String>() {

    init {
        put("country", country)
    }
}
