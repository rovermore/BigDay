package com.smallworldfs.moneytransferapp.data.account.contactsupport.repository.mappers

import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.model.ContactSupportDTO
import com.smallworldfs.moneytransferapp.domain.support.model.ContactSupportInfoResponse
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class ContactSupportDTOMapper @Inject constructor() {

    fun map(response: ContactSupportInfoResponse): ContactSupportDTO =
        ContactSupportDTO(
            response.contactSupportInfo?.phone ?: STRING_EMPTY,
            response.contactSupportInfo?.email ?: STRING_EMPTY
        )
}
