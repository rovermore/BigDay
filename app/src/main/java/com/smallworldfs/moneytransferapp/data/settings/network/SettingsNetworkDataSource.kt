package com.smallworldfs.moneytransferapp.data.settings.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.settings.model.AppConfigResponse
import com.smallworldfs.moneytransferapp.data.settings.model.SettingsRequest
import com.smallworldfs.moneytransferapp.data.settings.model.SettingsResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.modules.settings.domain.model.SettingsServer

class SettingsNetworkDataSource(
    private val service: SettingsService
) : NetworkDatasource() {

    fun requestSettings(country: String): OperationResult<SettingsResponse, APIError> =
        executeCall(service.requestSettings(SettingsRequest(country)))

    fun requestAppConfig(): OperationResult<AppConfigResponse, APIError> =
        executeCall(service.requestAppConfig())

    fun saveSettings(country: String, userId: String, userToken: String, accept: String): OperationResult<SettingsServer, APIError> =
        executeCall(
            service.saveMarketingPreferences(
                country,
                userId,
                userToken,
                accept,
                "engage",
            ),
        )
}
