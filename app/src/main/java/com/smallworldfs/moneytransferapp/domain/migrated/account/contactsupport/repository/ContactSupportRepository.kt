package com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.repository

import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.model.ContactSupportDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

interface ContactSupportRepository {

    fun getContactSupport(country: String): OperationResult<ContactSupportDTO, com.smallworldfs.moneytransferapp.domain.migrated.base.Error>
}
