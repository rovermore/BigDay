package com.smallworldfs.moneytransferapp.domain.support.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ContactSupportInfo

data class ContactSupportInfoResponse(
    var msg: String? = null,
    @SerializedName("data")
    var contactSupportInfo: ContactSupportInfo? = null
)
