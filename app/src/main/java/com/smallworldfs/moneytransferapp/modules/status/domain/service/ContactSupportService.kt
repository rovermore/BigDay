package com.smallworldfs.moneytransferapp.modules.status.domain.service

import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ContactSupportResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ContactSupportRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.SendEmailRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface ContactSupportService {

    @GET(EndPoint.CONTACT_INFO)
    fun getContactSupportInfo(@QueryMap request: ContactSupportRequest?): Observable<Response<ContactSupportResponse?>?>?

    @POST(EndPoint.SEND_EMAIL)
    fun sendEmail(@QueryMap request: SendEmailRequest?): Observable<Response<Void?>?>?
}
