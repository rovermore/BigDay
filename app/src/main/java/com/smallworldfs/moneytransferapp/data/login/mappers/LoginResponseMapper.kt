package com.smallworldfs.moneytransferapp.data.login.mappers

import com.smallworldfs.moneytransferapp.data.login.model.LoginResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class LoginResponseMapper @Inject constructor() {

    fun map(loginResponse: LoginResponse): OperationResult<UserDTO, Error> {
        with(loginResponse.data.user) {
            return Success(
                UserDTO(
                    id = id.toString(),
                    name = name ?: STRING_EMPTY,
                    surname = surname ?: STRING_EMPTY,
                    email = email ?: STRING_EMPTY,
                    country = CountriesDTO(mutableListOf(CountryDTO(country ?: STRING_EMPTY))),
                    status = status ?: STRING_EMPTY,
                    userToken = userToken ?: STRING_EMPTY,
                    uuid = uuid ?: STRING_EMPTY
                )
            )
        }
    }
}
