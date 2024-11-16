package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.CityDTO

object CitiesDTO {

    val cityDTO = CityDTO("madrid")

    val cityDTOList = listOf(cityDTO, cityDTO, cityDTO)
}
