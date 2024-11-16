package com.smallworldfs.moneytransferapp.data.address.model

data class AddressResponse(
    val data: List<Address>
) {
    data class Address(
        val id: String,
        val type: String,
        val detail: Detail
    )

    data class Detail(
        val text: String?,
        val description: String?
    )
}
