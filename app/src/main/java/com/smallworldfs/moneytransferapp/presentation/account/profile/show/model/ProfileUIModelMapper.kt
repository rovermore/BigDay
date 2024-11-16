package com.smallworldfs.moneytransferapp.presentation.account.profile.show.model

import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryDataMapper
import javax.inject.Inject

class ProfileUIModelMapper @Inject constructor(
    private val countryDataMapper: CountryDataMapper
) {

    fun map(user: UserDTO): ProfileUIModel {
        return with(user) {
            ProfileUIModel(
                id,
                clientId,
                countryDataMapper.map(user.country).countries,
                name,
                secondName,
                surname,
                secondSurname,
                phone,
                mobile,
                birthDate,
                cp,
                streetNumber,
                buildingName,
                address,
                city,
                receiveNewsletters,
                receiveStatusTrans
            )
        }
    }
}
