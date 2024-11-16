package com.smallworldfs.moneytransferapp.data.account.account.network

import com.smallworldfs.moneytransferapp.data.account.account.model.AccountMenuResponse
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

class AccountNetworkDatasource(private val service: AccountDataService) : NetworkDatasource() {

    fun getAccountMenu(userToken: String, userId: String): OperationResult<AccountMenuResponse, APIError> =
        executeCall(service.getAccountMenu(userToken, userId))
}
