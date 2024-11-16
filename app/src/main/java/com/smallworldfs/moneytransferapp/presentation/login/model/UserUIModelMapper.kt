package com.smallworldfs.moneytransferapp.presentation.login.model

import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryDataMapper
import javax.inject.Inject

class UserUIModelMapper @Inject constructor(
    private val countryDataMapper: CountryDataMapper
) {
    fun map(userDTO: UserDTO, createPassCode: Boolean): UserUIModel {
        return UserUIModel(
            userDTO.id,
            userDTO.clientId,
            userDTO.name,
            userDTO.secondName,
            userDTO.surname,
            userDTO.secondSurname,
            userDTO.email,
            countryDataMapper.map(userDTO.country),
            userDTO.birthDate,
            userDTO.phone,
            userDTO.mobile,
            userDTO.address,
            userDTO.cp,
            userDTO.city,
            userDTO.status,
            userDTO.streetNumber,
            userDTO.buildingName,
            userDTO.appToken,
            userDTO.mobilePhoneCountry,
            userDTO.userToken,
            userDTO.kountsessid,
            userDTO.flinksState,
            userDTO.gdpr,
            userDTO.freshchatId,
            createPassCode
        )
    }
}
