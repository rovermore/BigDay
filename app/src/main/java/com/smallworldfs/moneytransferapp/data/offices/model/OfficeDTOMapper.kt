package com.smallworldfs.moneytransferapp.data.offices.model

import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.OfficeDTO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class OfficeDTOMapper @Inject constructor() {
    fun mapFromBranches(officesResponse: OfficesResponse): List<OfficeDTO> {
        val officeDTOList = mutableListOf<OfficeDTO>()
        officesResponse.branches.forEach {
            officeDTOList.add(
                OfficeDTO(
                    address = it.address ?: STRING_EMPTY,
                    city = it.city ?: STRING_EMPTY,
                    country = it.country ?: STRING_EMPTY,
                    cp = it.cp ?: STRING_EMPTY,
                    email = it.email ?: STRING_EMPTY,
                    fax = it.fax ?: STRING_EMPTY,
                    festive = it.festive ?: STRING_EMPTY,
                    latitude = it.latitude ?: STRING_EMPTY,
                    longitude = it.longitude ?: STRING_EMPTY,
                    name = it.name ?: STRING_EMPTY,
                    phone = it.phone ?: STRING_EMPTY,
                    province = it.province ?: STRING_EMPTY,
                    timetable1 = it.timetable1 ?: STRING_EMPTY,
                    timetable2 = it.timetable2 ?: STRING_EMPTY
                )
            )
        }
        return officeDTOList
    }

    fun mapFromLocations(officesPoiResponse: OfficesPoiResponse): List<OfficeDTO> {
        val officeDTOList = mutableListOf<OfficeDTO>()
        officesPoiResponse.locations.forEach {
            officeDTOList.add(
                OfficeDTO(
                    address = it.address ?: STRING_EMPTY,
                    city = it.city ?: STRING_EMPTY,
                    cp = it.cp ?: STRING_EMPTY,
                    email = it.email ?: STRING_EMPTY,
                    fax = it.fax ?: STRING_EMPTY,
                    festive = it.festive ?: STRING_EMPTY,
                    id = it.id ?: STRING_EMPTY,
                    latitude = it.latitude ?: STRING_EMPTY,
                    longitude = it.longitude ?: STRING_EMPTY,
                    miles = it.miles ?: STRING_EMPTY,
                    name = it.name ?: STRING_EMPTY,
                    officeCode = it.office_code ?: STRING_EMPTY,
                    phone = it.phone ?: STRING_EMPTY,
                    province = it.province ?: STRING_EMPTY,
                    timetable1 = it.timetable1 ?: STRING_EMPTY,
                    timetable2 = it.timetable2 ?: STRING_EMPTY,
                    url = it.url ?: STRING_EMPTY
                )
            )
        }
        return officeDTOList
    }
}
