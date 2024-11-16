package com.smallworldfs.moneytransferapp.domain.migrated.signup.model

data class MarketingPreferenceDTO(val enable: List<MarketingPreference>, val disable: List<MarketingPreference>)
sealed class MarketingPreference {
    object SMS : MarketingPreference()
    object Email : MarketingPreference()
    object PushNotification : MarketingPreference()
}
