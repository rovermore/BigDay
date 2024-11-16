package com.smallworldfs.moneytransferapp.presentation.login.model

import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.presentation.common.CountriesData
import com.smallworldfs.moneytransferapp.presentation.common.CountryData
import javax.inject.Inject

class UserDTOMapper @Inject constructor() {
    fun map(userDTO: UserDTO): UserData {
        val countriesData = CountriesData()
        userDTO.country.countries.forEach { countriesData.countries.add(CountryData(it.iso3, it.name)) }
        return UserData(
            userDTO.id,
            userDTO.clientId,
            userDTO.name,
            userDTO.secondName,
            userDTO.surname,
            userDTO.secondSurname,
            userDTO.email,
            countriesData,
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
            userDTO.freshchatId
        )
    }
}
