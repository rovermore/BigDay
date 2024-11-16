package com.smallworldfs.moneytransferapp.data.account.contactsupport.repository.network

import com.smallworldfs.moneytransferapp.domain.support.model.ContactSupportInfoResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ContactSupportService {

    @GET(EndPoint.CONTACT_INFO)
    fun requestContactSupportInfo(@Query("country") country: String): Call<ContactSupportInfoResponse>
}
