package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO

object UserDTOMock {

    val userDTO = UserDTO(
        id = "fn43p9fuhe",
        uuid = "dbsjka0",
        clientId = "543534",
        name = "Tester",
        secondName = "Test",
        email = "roberto@test.com",
        country = CountriesDTO(
            mutableListOf(
                CountryDTO(
                    iso3 = "ES",
                    name = "España",
                    phonePrefix = "+34",
                    countryCode = "34"
                )
            )
        ),
        userToken = "dsgagfad",
        status = UserDTO.APPROVED
    )
}
