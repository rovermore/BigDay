package com.smallworldfs.moneytransferapp.data.login.mappers

import com.smallworldfs.moneytransferapp.data.login.model.LimitedLoginResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class LimitedUserMapper @Inject constructor() {
    fun map(loginResponse: LimitedLoginResponse): OperationResult<UserDTO, Error> {
        loginResponse.data.user.let { limitedUser ->
            return Success(
                UserDTO(
                    id = limitedUser.id,
                    uuid = limitedUser.uuid,
                    name = limitedUser.name ?: STRING_EMPTY,
                    surname = limitedUser.surname ?: STRING_EMPTY,
                    email = limitedUser.email ?: STRING_EMPTY,
                    country = CountriesDTO(mutableListOf(CountryDTO(iso3 = limitedUser.country))),
                    status = limitedUser.status,
                    userToken = limitedUser.user_token,
                    appToken = limitedUser.appToken
                )
            )
        }
    }
}
