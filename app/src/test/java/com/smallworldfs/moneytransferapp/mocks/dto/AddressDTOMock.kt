package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.addess.model.AddressDTO

object AddressDTOMock {

    val addressDTO = AddressDTO(
        "25423",
        "1",
        AddressDTO.Detail(
            "Gran via 30",
            "Madrid 28015"
        )
    )

    val addressDTO2 = AddressDTO(
        "25523",
        "3",
        AddressDTO.Detail(
            "Gran via 30",
            "Alicante"
        )
    )

    val addressDTOList = listOf<AddressDTO>(addressDTO, addressDTO2)
}
