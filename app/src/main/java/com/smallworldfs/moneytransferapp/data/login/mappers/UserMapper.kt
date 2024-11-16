package com.smallworldfs.moneytransferapp.data.login.mappers

import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.modules.customization.domain.repository.AppCustomizationRepository
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import java.util.TreeMap
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun mapFromUserDTO(userDTO: UserDTO): User {
        val dtoTreemapCountry = TreeMap<String, String>()
        val dtoTreemapPhone = TreeMap<String, String>()
        val countryAvailable = userDTO.country.countries.isNotEmpty()
        if (countryAvailable) {
            dtoTreemapCountry[userDTO.country.countries[0].iso3] = userDTO.country.countries[0].name
        } else {
            dtoTreemapCountry[AppCustomizationRepository.getInstance().countryCodeSelected.first] = AppCustomizationRepository.getInstance().countryCodeSelected.second
        }
        val user = User()
        user.id = userDTO.id
        user.clientId = userDTO.clientId
        user.name = userDTO.name
        user.secondName = userDTO.secondName
        user.surname = userDTO.surname
        user.secondSurname = userDTO.secondSurname
        user.email = userDTO.email
        user.country = dtoTreemapCountry
        user.birthDate = userDTO.birthDate
        user.phone = userDTO.phone
        user.mobile = userDTO.mobile
        user.address = userDTO.address
        user.cp = userDTO.cp
        user.city = userDTO.city
        user.status = userDTO.status
        user.streetNumber = userDTO.streetNumber
        user.buildingName = userDTO.buildingName
        user.appToken = userDTO.appToken
        user.mobilePhoneCountry = dtoTreemapPhone
        user.userToken = userDTO.userToken
        user.kountsessid = userDTO.kountsessid
        user.finishedTransactions = userDTO.finishedTransactions
        user.flinksState = userDTO.flinksState
        user.gdpr = userDTO.gdpr
        user.freshchatId = userDTO.freshchatId

        return user
    }
}
