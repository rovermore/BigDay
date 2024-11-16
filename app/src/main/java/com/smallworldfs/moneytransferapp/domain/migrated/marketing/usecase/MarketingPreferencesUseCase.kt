package com.smallworldfs.moneytransferapp.domain.migrated.marketing.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreferenceDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form
import javax.inject.Inject

class MarketingPreferencesUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
) {

    fun requestPreferences(formView: String): OperationResult<Form, Error> =
        userDataRepository.requestMarketingPreferences(formView)

    fun updatePreferences(marketingPreferenceDTO: MarketingPreferenceDTO) =
        userDataRepository.updateMarketingPreferences(marketingPreferenceDTO)
}
