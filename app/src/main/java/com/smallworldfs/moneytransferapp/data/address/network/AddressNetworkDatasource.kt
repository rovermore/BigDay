package com.smallworldfs.moneytransferapp.data.address.network

import com.smallworldfs.moneytransferapp.data.address.model.AddressFormResponse
import com.smallworldfs.moneytransferapp.data.address.model.AddressResponse
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

class AddressNetworkDatasource(private val service: AddressService) : NetworkDatasource() {

    fun searchAddress(
        lang: String,
        text: String,
        country: String,
        userToken: String,
        uuid: String
    ): OperationResult<AddressResponse, APIError> =
        executeCall(service.searchAddress(lang, text, country, userToken, uuid))

    fun searchAddressByParentId(
        lang: String,
        parentId: String,
        country: String,
        userToken: String,
        uuid: String
    ): OperationResult<AddressResponse, APIError> =
        executeCall(service.searchAddressByParentId(lang, parentId, country, userToken, uuid))

    fun getAddressById(
        lang: String,
        id: String,
        country: String,
        userToken: String,
        uuid: String
    ): OperationResult<AddressFormResponse, APIError> =
        executeCall(service.getAddressById(lang, id, country, userToken, uuid))

    fun getAddressForm(
        lang: String,
        country: String,
        userToken: String,
        uuid: String
    ): OperationResult<AddressFormResponse, APIError> =
        executeCall(service.getAddressForm(lang, country, userToken, uuid))
}
