package com.smallworldfs.moneytransferapp.data.login.mappers

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.modules.login.domain.model.Gdpr
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class UserDTOMapper @Inject constructor() {

    fun map(user: User?): OperationResult<UserDTO, Error> {
        return if (user != null &&
            !user.country.isNullOrEmpty() && !user.country.firstEntry()?.value.isNullOrEmpty() &&
            !user.userToken.isNullOrEmpty()
        ) {
            val countries = CountriesDTO()
            val key: String = user.country.firstKey()
            val value: String = user.country.getValue(key)
            countries.countries.add(CountryDTO(key, value))
            val destination = CountryDTO(user.destinationCountry?.firstEntry()?.key ?: "", user.destinationCountry?.firstEntry()?.value ?: "")
            return Success(
                UserDTO(
                    user.id ?: STRING_EMPTY,
                    user.clientId ?: STRING_EMPTY,
                    user.name ?: STRING_EMPTY,
                    user.secondName ?: STRING_EMPTY,
                    user.surname ?: STRING_EMPTY,
                    user.secondSurname ?: STRING_EMPTY,
                    user.email ?: STRING_EMPTY,
                    countries,
                    destination,
                    user.birthDate ?: STRING_EMPTY,
                    user.phone ?: STRING_EMPTY,
                    user.mobile ?: STRING_EMPTY,
                    user.address ?: STRING_EMPTY,
                    user.cp ?: STRING_EMPTY,
                    user.city ?: STRING_EMPTY,
                    user.status ?: STRING_EMPTY,
                    user.streetNumber ?: STRING_EMPTY,
                    user.buildingName ?: STRING_EMPTY,
                    user.appToken ?: STRING_EMPTY,
                    user.mobilePhoneCountry.firstKey(),
                    user.userToken ?: STRING_EMPTY,
                    user.kountsessid ?: STRING_EMPTY,
                    user.flinksState ?: STRING_EMPTY,
                    user.gdpr ?: Gdpr(),
                    user.freshchatId ?: STRING_EMPTY,
                    user.finishedTransactions ?: STRING_EMPTY
                )
            )
        } else Failure(Error.Unmapped("Required fields are not present"))
    }

    fun mapOrNull(user: User?): UserDTO? {
        return if (user != null &&
            !user.country.isNullOrEmpty() && !user.country.firstEntry()?.key.isNullOrEmpty() &&
            !user.userToken.isNullOrEmpty()
        ) {
            val countries = CountriesDTO()
            val key: String = user.country.firstKey()
            val value: String = user.country.getValue(key)
            countries.countries.add(CountryDTO(key, value))
            val destination = CountryDTO(user.destinationCountry?.firstEntry()?.key ?: "", user.destinationCountry?.firstEntry()?.value ?: "")
            return UserDTO(
                user.id ?: STRING_EMPTY,
                user.clientId ?: STRING_EMPTY,
                user.name ?: STRING_EMPTY,
                user.secondName ?: STRING_EMPTY,
                user.surname ?: STRING_EMPTY,
                user.secondSurname ?: STRING_EMPTY,
                user.email ?: STRING_EMPTY,
                countries,
                destination,
                user.birthDate ?: STRING_EMPTY,
                user.phone ?: STRING_EMPTY,
                user.mobile ?: STRING_EMPTY,
                user.address ?: STRING_EMPTY,
                user.cp ?: STRING_EMPTY,
                user.city ?: STRING_EMPTY,
                user.status ?: STRING_EMPTY,
                user.streetNumber ?: STRING_EMPTY,
                user.buildingName ?: STRING_EMPTY,
                user.appToken ?: STRING_EMPTY,
                user.mobilePhoneCountry.firstEntry()?.key ?: STRING_EMPTY,
                user.userToken ?: STRING_EMPTY,
                user.kountsessid ?: STRING_EMPTY,
                user.flinksState ?: STRING_EMPTY,
                user.gdpr ?: Gdpr(),
                user.freshchatId ?: STRING_EMPTY,
                user.finishedTransactions ?: STRING_EMPTY,
            )
        } else null
    }
}
