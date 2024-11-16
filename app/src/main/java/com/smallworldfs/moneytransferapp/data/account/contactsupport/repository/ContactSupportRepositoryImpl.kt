package com.smallworldfs.moneytransferapp.data.account.contactsupport.repository

import com.smallworldfs.moneytransferapp.data.account.contactsupport.repository.mappers.ContactSupportDTOMapper
import com.smallworldfs.moneytransferapp.data.account.contactsupport.repository.network.ContactSupportNetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.model.ContactSupportDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.repository.ContactSupportRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import javax.inject.Inject

class ContactSupportRepositoryImpl @Inject constructor(
    private val contactSupportNetworkDatasource: ContactSupportNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper,
    private val contactSupportDTOMapper: ContactSupportDTOMapper
) : ContactSupportRepository {

    override fun getContactSupport(country: String): OperationResult<ContactSupportDTO, Error> {
        return contactSupportNetworkDatasource.requestContactSupport(country)
            .map {
                return Success(contactSupportDTOMapper.map(it))
            }
            .mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }
}
