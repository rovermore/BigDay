package com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class ResponseCashPickUpChooseLocationDataModel(
    var msg: String? = STRING_EMPTY,
    @SerializedName("representatives")
    var locationResponses: List<ResponseCashPickUpLocationDataModel>? = listOf()
)
