package com.smallworldfs.moneytransferapp.data.marketing.model

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest

data class SaveMarketingPreferencesRequest(
    private val userToken: String,
    private val userId: String,
    private val marketingPreferences: HashMap<String, String>
) : ServerQueryMapRequest() {

    init {
        put("userId", userId)
        put("userToken", userToken)
        put("type", "preferences")
        marketingPreferences.forEach { (key, value) -> put(key, value) }
    }
}
