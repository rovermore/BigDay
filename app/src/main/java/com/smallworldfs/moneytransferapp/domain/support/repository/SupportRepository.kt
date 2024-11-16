package com.smallworldfs.moneytransferapp.domain.support.repository

import com.smallworldfs.moneytransferapp.domain.support.model.ContactSupportInfoRequest
import com.smallworldfs.moneytransferapp.domain.support.model.ContactSupportInfoResponse
import com.smallworldfs.moneytransferapp.domain.support.model.SendEmailRequest
import com.smallworldfs.moneytransferapp.domain.support.repository.network.SupportNetwork
import io.reactivex.Observable
import javax.inject.Inject

class SupportRepository @Inject constructor() {

    @Inject
    lateinit var supportNetwork: SupportNetwork

    fun requestContactSupportInfo(request: ContactSupportInfoRequest): Observable<ContactSupportInfoResponse> =
        supportNetwork.requestContactSupportInfo(request)

    fun requestCall(request: SendEmailRequest): Observable<Unit> =
        supportNetwork.sendEmail(request)
}
