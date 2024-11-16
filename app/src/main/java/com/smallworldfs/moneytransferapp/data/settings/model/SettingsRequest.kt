package com.smallworldfs.moneytransferapp.data.settings.model

class SettingsRequest(country: String) : HashMap<String, String>() {

    init {
        put("country", country)
    }
}
