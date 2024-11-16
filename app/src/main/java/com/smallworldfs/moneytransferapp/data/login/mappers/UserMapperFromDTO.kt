package com.smallworldfs.moneytransferapp.data.login.mappers

import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import java.util.TreeMap
import javax.inject.Inject

class UserMapperFromDTO @Inject constructor() {

    fun mapFromUserDTO(userDTO: UserDTO): User {
        val dtoTreemapCountry = TreeMap<String, String>()
        val dtoTreemapDestinationCountry = TreeMap<String, String>()
        val dtoTreemapPhone = TreeMap<String, String>()
        if (userDTO.country.countries.isNotEmpty()) dtoTreemapCountry.put(userDTO.country.countries[0].iso3, userDTO.country.countries[0].name)
        dtoTreemapDestinationCountry.put(userDTO.destinationCountry.iso3, userDTO.destinationCountry.name)
        dtoTreemapPhone.put(userDTO.mobilePhoneCountry, userDTO.mobilePhoneCountry)
        val user = User()
        user.id = userDTO.id
        user.clientId = userDTO.clientId
        user.name = userDTO.name
        user.secondName = userDTO.secondName
        user.surname = userDTO.surname
        user.secondSurname = userDTO.secondSurname
        user.email = userDTO.email
        user.country = dtoTreemapCountry
        user.destinationCountry = dtoTreemapDestinationCountry
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
        user.showEmailValidated = userDTO.showEmailValidated
        user.receiveNewsletters = if (userDTO.receiveNewsletters) "1" else "0"
        user.authenticated = userDTO.authenticated
        return user
    }
}
