package com.smallworldfs.moneytransferapp.data.userdata.model

import com.smallworldfs.moneytransferapp.data.login.model.UserResponse
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.model.UserStatusDTO
import com.smallworldfs.moneytransferapp.modules.login.domain.model.Gdpr
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class UserResponseMapper @Inject constructor() {

    fun mapStatus(status: String?): UserStatusDTO {
        return if (status != null) {
            when (status) {
                "PDT_SMS" -> UserStatusDTO.SmsPending(status)
                "NO_CLIENT_ID" -> UserStatusDTO.NoClientId(status)
                "APPROVED_PDT_EMAIL" -> UserStatusDTO.ApprovedEmailPending(status)
                "PDT_PROFILE" -> UserStatusDTO.ProfilePending(status)
                "APPROVED" -> UserStatusDTO.Approved(status)
                else -> UserStatusDTO.Unknown("Unknown")
            }
        } else UserStatusDTO.Unknown("Unknown")
    }

    fun map(userResponse: UserResponse): UserDTO {
        with(userResponse.data.user) {
            return UserDTO(
                id = id.toString(),
                clientId = clientId ?: STRING_EMPTY,
                name = name ?: STRING_EMPTY,
                secondName = secondName ?: STRING_EMPTY,
                surname = surname ?: STRING_EMPTY,
                secondSurname = secondSurname ?: STRING_EMPTY,
                email = email,
                country = CountriesDTO(mutableListOf(CountryDTO(country))),
                birthDate = birthDate ?: STRING_EMPTY,
                phone = phone ?: STRING_EMPTY,
                mobile = mobile ?: STRING_EMPTY,
                address = address ?: STRING_EMPTY,
                cp = cp ?: STRING_EMPTY,
                city = city ?: STRING_EMPTY,
                status = status,
                streetNumber = streetNumber ?: STRING_EMPTY,
                buildingName = buildingName ?: STRING_EMPTY,
                appToken = appTokken ?: STRING_EMPTY,
                mobilePhoneCountry = mobilePhoneCountry ?: STRING_EMPTY,
                userToken = userToken,
                kountsessid = kountsessid ?: STRING_EMPTY,
                flinksState = flinksState.toString(),
                gdpr = Gdpr(),
                freshchatId = freshchatId ?: STRING_EMPTY,
                finishedTransactions = finishedTransactions ?: STRING_EMPTY,
                uuid = uuid,
                emailValidated = validatedEmail,
                receiveNewsletters = (receiveNewsletters ?: STRING_EMPTY) == "1",
                receiveStatusTrans = (receiveStatusTrans ?: STRING_EMPTY) == "1",
                authenticated = (authenticationStatus ?: STRING_EMPTY) == "OK"
            )
        }
    }
}
