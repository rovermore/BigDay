package com.smallworldfs.moneytransferapp.data.userdata.model

import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreference
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreferenceDTO
import com.smallworldfs.moneytransferapp.utils.toNumberString
import javax.inject.Inject

class SaveMarketingPreferencesRequestMapper @Inject constructor() {

    fun map(marketingPreferenceDTO: MarketingPreferenceDTO): HashMap<String, String> {
        val preferences = HashMap<String, String>()
        marketingPreferenceDTO.enable.map {
            when (it) {
                is MarketingPreference.SMS -> preferences.put("accept_sms", true.toNumberString())
                is MarketingPreference.Email -> preferences.put("accept_email", true.toNumberString())
                else -> preferences.put("accept_push", true.toNumberString())
            }
        }
        marketingPreferenceDTO.disable.map {
            when (it) {
                is MarketingPreference.SMS -> preferences.put("accept_sms", false.toNumberString())
                is MarketingPreference.Email -> preferences.put("accept_email", false.toNumberString())
                else -> preferences.put("accept_push", false.toNumberString())
            }
        }
        return preferences
    }
}
