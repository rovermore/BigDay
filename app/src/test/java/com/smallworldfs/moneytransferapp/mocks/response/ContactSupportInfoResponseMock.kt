package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.domain.support.model.ContactSupportInfoResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ContactSupportInfo

object ContactSupportInfoResponseMock {

    private val contactSupportInfo = ContactSupportInfo().apply {
        livezilla = "livezilla"
        phone = "phone"
        address = "address"
    }

    val contactSupportInfoResponse = ContactSupportInfoResponse("msg", contactSupportInfo)
}
