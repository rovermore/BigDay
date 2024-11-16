package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.StateDTO

object StateDTOMock {
    val stateDTO = StateDTO(
        "sggff43",
        "kansas",
        "urlLogo",
        true
    )

    val stateDTO2 = StateDTO(
        "sggff43",
        "California",
        "urlLogo",
        false
    )

    val stateList = listOf<StateDTO>(stateDTO, stateDTO2)
}
