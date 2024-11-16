package com.smallworldfs.moneytransferapp.data.promotions.model

import com.google.gson.annotations.SerializedName

data class PromotionResponse(
    var msg: String?,
    @SerializedName("result") var promotions: List<Promotion?>?
)

data class Promotion(
    var promotionName: String?,
    var promotionCode: String?,
    var type: String?,
    var countryOrigin: String?,
    var countryDestination: String?,
    var discount: String?,
    var expireDate: String?,
    @SerializedName("discount_type") var discountType: String?,
)
