package com.smallworldfs.moneytransferapp.data.address.network

import com.smallworldfs.moneytransferapp.data.address.model.AddressFormResponse
import com.smallworldfs.moneytransferapp.data.address.model.AddressResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AddressService {

    @GET(EndPoint.ADDRESS_SEARCH)
    fun searchAddress(
        @Path("lang") lang: String,
        @Query("query") text: String,
        @Query("country") country: String,
        @Query("userToken") userToken: String,
        @Query("uuid") uuid: String
    ): Call<AddressResponse>

    @GET(EndPoint.ADDRESS_SEARCH)
    fun searchAddressByParentId(
        @Path("lang") lang: String,
        @Query("parentId") parentId: String,
        @Query("country") country: String,
        @Query("userToken") userToken: String,
        @Query("uuid") uuid: String
    ): Call<AddressResponse>

    @GET(EndPoint.ADDRESS_ID)
    fun getAddressById(
        @Path("lang") lang: String,
        @Path("addressID") addressID: String,
        @Query("country") country: String,
        @Query("userToken") userToken: String,
        @Query("uuid") uuid: String
    ): Call<AddressFormResponse>

    @GET(EndPoint.ADDRESS_FORM)
    fun getAddressForm(
        @Path("lang") lang: String,
        @Query("country") country: String,
        @Query("userToken") userToken: String,
        @Query("uuid") uuid: String
    ): Call<AddressFormResponse>
}
