package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.OfficeDTO

object OfficesDTOListMock {

    val office1 = OfficeDTO(
        name = "A Coru\u00f1a",
        country = "ESP",
        city = "A Coru\u00f1a",
        province = "A Coru\u00f1a",
        cp = "15007",
        address = "C/ Pintor Germ\u00e1nTaibo, 17",
        phone = "+34 981 230 427",
        fax = "34981230128",
        email = "televentas@smallworldfs.com",
        latitude = "43.355984",
        longitude = "-8.412609",
        timetable1 = "L-S 10:00-20:00",
        timetable2 = "null",
        festive = "null"
    )

    val office2 = OfficeDTO(
        name = "A Coru\u00f1a",
        country = "ESP",
        city = "A Coru\u00f1a",
        province = "A Coru\u00f1a",
        cp = "15007",
        address = "C/ Pintor Germ\u00e1nTaibo, 17",
        phone = "+34 981 230 427",
        fax = "34981230128",
        email = "televentas@smallworldfs.com",
        latitude = "43.355984",
        longitude = "-8.412609",
        timetable1 = "L-S 10:00-20:00",
        timetable2 = "null",
        festive = "null"
    )

    val officeDTOList = listOf(office1, office2)
}
