package com.smallworldfs.moneytransferapp.presentation.account.offices.model

import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.OfficeDTO
import com.smallworldfs.moneytransferapp.presentation.common.coordinates.SWCoordinates
import javax.inject.Inject

class OfficesUIModelMapper @Inject constructor() {
    fun map(officesDTOList: List<OfficeDTO>): List<OfficeUIModel> {
        val officeUIModelList = mutableListOf<OfficeUIModel>()
        officesDTOList.forEach {
            officeUIModelList.add(
                OfficeUIModel(
                    address = it.address,
                    city = it.city,
                    country = it.country,
                    cp = it.cp,
                    email = it.email,
                    fax = it.fax,
                    festive = it.festive,
                    location = mapCoordinates(it.latitude, it.longitude),
                    name = it.name,
                    phone = it.phone,
                    province = it.province,
                    timetable1 = it.timetable1,
                    timetable2 = it.timetable2
                )
            )
        }
        return officeUIModelList
    }

    fun mapOfficePoi(officePoiList: List<OfficeDTO>): List<OfficeUIModel> {
        val officeUIModelList = mutableListOf<OfficeUIModel>()
        officePoiList.forEach {
            officeUIModelList.add(
                OfficeUIModel(
                    address = it.address,
                    city = it.city,
                    cp = it.cp,
                    email = it.email,
                    fax = it.fax,
                    festive = it.festive,
                    id = it.id,
                    location = mapCoordinates(it.latitude, it.longitude),
                    miles = it.miles,
                    name = it.name,
                    officeCode = it.officeCode,
                    phone = it.phone,
                    province = it.province,
                    timetable1 = it.timetable1,
                    timetable2 = it.timetable2,
                    url = it.url
                )
            )
        }
        return officeUIModelList
    }

    private fun mapCoordinates(lat: String, long: String) = try {
        SWCoordinates.LatitudeLongitude(lat.toDouble(), long.toDouble())
    } catch (exception: Exception) { SWCoordinates.NotDefined }
}
