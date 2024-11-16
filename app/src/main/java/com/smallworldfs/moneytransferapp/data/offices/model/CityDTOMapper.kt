package com.smallworldfs.moneytransferapp.data.offices.model

import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.CityDTO
import javax.inject.Inject

class CityDTOMapper @Inject constructor() {
    fun map(cityList: ArrayList<HashMap<String, String>>): List<CityDTO> {
        val cityDTOList = mutableListOf<CityDTO>()
        cityList.forEach { cityDTOList.add(CityDTO(it.values.first())) }
        return cityDTOList
    }
}
