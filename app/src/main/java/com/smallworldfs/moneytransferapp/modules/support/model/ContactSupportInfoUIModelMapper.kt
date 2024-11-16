package com.smallworldfs.moneytransferapp.modules.support.model

import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.model.ContactSupportDTO
import javax.inject.Inject

class ContactSupportInfoUIModelMapper @Inject constructor() {

    fun map(contactSupportDTO: ContactSupportDTO): ContactSupportInfoUIModel =
        with(contactSupportDTO) {
            ContactSupportInfoUIModel(
                phone,
                email,
                isLimitedUser
            )
        }
}
