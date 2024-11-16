package com.smallworldfs.moneytransferapp.data.account.contactsupport.repository.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.support.model.ContactSupportInfoResponse
import javax.inject.Inject

class ContactSupportNetworkDatasource @Inject constructor(
    private val service: ContactSupportService
) : NetworkDatasource() {

    fun requestContactSupport(country: String): OperationResult<ContactSupportInfoResponse, APIError> =
        executeCall(
            service.requestContactSupportInfo(country)
        )
}
