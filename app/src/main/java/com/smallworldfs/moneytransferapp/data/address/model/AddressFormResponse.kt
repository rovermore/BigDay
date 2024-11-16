package com.smallworldfs.moneytransferapp.data.address.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field

class AddressFormResponse(
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("form")
        val form: List<Field>
    )
}
