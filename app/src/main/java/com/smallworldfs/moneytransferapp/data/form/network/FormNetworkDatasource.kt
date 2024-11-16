package com.smallworldfs.moneytransferapp.data.form.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.form.model.FormContent
import com.smallworldfs.moneytransferapp.data.login.network.FormService
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

class FormNetworkDatasource(
    private val service: FormService
) : NetworkDatasource() {

    fun getFieldContent(url: String): OperationResult<FormContent, APIError> =
        executeCall(service.getFormContent(url))
}
