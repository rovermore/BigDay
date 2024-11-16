package com.smallworldfs.moneytransferapp.modules.transactional.domain.service

import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.LocationMapResponse
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.ClearTransactionRequest
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.LocationMapRequest
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.QuickReminderRequest
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.ServerTransactionalRequest
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.ValidateTransactionRequest
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.TransactionalStepResponse
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderResponse
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationStepResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface TransactionalService {

    @GET(EndPoint.TRANSACTIONAL_STEPS)
    fun getSteps(
        @QueryMap request: ServerTransactionalRequest?
    ): Observable<Response<TransactionalStepResponse>?>?

    @POST(EndPoint.VALIDATE_STEP)
    fun validateStep(
        @QueryMap request: ValidateTransactionRequest?
    ): Observable<Response<ValidationStepResponse>?>?

    @DELETE(EndPoint.CLEARTRANSACTION)
    fun clearTransaction(
        @QueryMap request: ClearTransactionRequest?
    ): Observable<Response<Void>?>?

    @GET(EndPoint.QUICK_REMINDER_INFO)
    fun getQuickReminderInfo(
        @QueryMap request: QuickReminderRequest?
    ): Observable<Response<QuickReminderResponse>?>?

    @GET(EndPoint.REPRESENTATIVE_LOCATIONS_INFO)
    fun getLocationMapInfo(
        @QueryMap request: LocationMapRequest?
    ): Observable<Response<LocationMapResponse>?>?
}
