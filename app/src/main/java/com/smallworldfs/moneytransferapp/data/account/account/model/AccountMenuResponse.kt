package com.smallworldfs.moneytransferapp.data.account.account.model

import com.google.gson.annotations.SerializedName

data class AccountMenuResponse(
    val data: Data
)
data class Data(
    val blocks: MutableList<AccountMenuItemResponse> = mutableListOf(),
    val rows: MutableList<AccountMenuItemResponse> = mutableListOf(),
    @SerializedName("limited_user_rows")
    val limitedRows: MutableList<AccountMenuItemResponse> = mutableListOf()
)
data class AccountMenuItemResponse(
    val type: String?,
    val position: String?,
    val title: String?,
    val description: String?,
    val active: Boolean?,
    @SerializedName("num_info")
    val numInfo: Int?,
    @SerializedName("num_new_info")
    val numNewInfo: Int?
)
