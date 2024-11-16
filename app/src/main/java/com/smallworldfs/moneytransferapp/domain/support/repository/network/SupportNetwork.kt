package com.smallworldfs.moneytransferapp.domain.support.repository.network

import com.smallworldfs.moneytransferapp.domain.support.model.ContactSupportInfoRequest
import com.smallworldfs.moneytransferapp.domain.support.model.ContactSupportInfoResponse
import com.smallworldfs.moneytransferapp.domain.support.model.SendEmailRequest
import io.reactivex.Observable
import javax.inject.Inject

class SupportNetwork @Inject constructor(private val service: SupportService) {

    fun requestContactSupportInfo(request: ContactSupportInfoRequest): Observable<ContactSupportInfoResponse> =
        service.requestContactSupportInfo(request)

    fun sendEmail(request: SendEmailRequest): Observable<Unit> =
        service.requestCall(request)
}
