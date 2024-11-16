package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.offices.model.OfficeCountryResponse

object OfficeCountryResponseMock {

    private val country = HashMap<String, String>().apply {
        put("es", "ESP")
        put("en", "GBT")
    }

    private val countries = arrayListOf(country, country)

    val officeCountryResponse = OfficeCountryResponse("msg", countries)
}
