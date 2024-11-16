package com.smallworldfs.moneytransferapp.domain.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

class GdprModel(
    @SerializedName("content")
    var listGdprMessages: ArrayList<QuickReminderMessageModel>? = arrayListOf(),
    var msg: String? = STRING_EMPTY,
    var title: String? = STRING_EMPTY,
    var type: String? = STRING_EMPTY,

    @SerializedName("button_ok_title")
    var buttonOkTitle: String? = STRING_EMPTY,

    @SerializedName("button_cancel_title")
    var buttonCancelTitle: String? = STRING_EMPTY
)
