package com.smallworldfs.moneytransferapp.domain.support.repository.network

import com.smallworldfs.moneytransferapp.domain.support.model.ContactSupportInfoRequest
import com.smallworldfs.moneytransferapp.domain.support.model.ContactSupportInfoResponse
import com.smallworldfs.moneytransferapp.domain.support.model.SendEmailRequest
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface SupportService {

    @GET(EndPoint.CONTACT_INFO)
    fun requestContactSupportInfo(@QueryMap request: ContactSupportInfoRequest): Observable<ContactSupportInfoResponse>

    @POST(EndPoint.SEND_EMAIL)
    fun requestCall(@QueryMap request: SendEmailRequest): Observable<Unit>
}
