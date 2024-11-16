package com.smallworldfs.moneytransferapp.domain.migrated.addess.model

data class AddressDTO(
    val id: String,
    val type: String,
    val detail: Detail
) {
    data class Detail(
        val text: String,
        val description: String
    )
}
