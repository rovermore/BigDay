package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.offices.model.CityResponse

object CityResponseMock {

    private val city = HashMap<String, String>().apply {
        put("mad", "madrid")
    }

    private val cities = arrayListOf(city, city)

    val cityResponseMock = CityResponse("msg", cities)
}
