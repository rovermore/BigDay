package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreference
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreferenceDTO
import com.smallworldfs.moneytransferapp.utils.toNumberString

object MarketingPreferenceDTOMock {
    private val enable = listOf(MarketingPreference.Email, MarketingPreference.SMS)
    private val disable = listOf(MarketingPreference.PushNotification)
    val marketingPreferencesDTO = MarketingPreferenceDTO(enable, disable)

    val hashMapPreferences = HashMap<String, String>().apply {
        put("accept_sms", true.toNumberString())
        put("accept_email", true.toNumberString())
        put("accept_push", false.toNumberString())
    }
}
