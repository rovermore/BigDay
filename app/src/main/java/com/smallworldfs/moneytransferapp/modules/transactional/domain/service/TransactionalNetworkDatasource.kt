package com.smallworldfs.moneytransferapp.modules.transactional.domain.service

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
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
import rx.Observable

class TransactionalNetworkDatasource(
    private val service: TransactionalService
) : NetworkDatasource() {

    fun requestTransactionalStructureSteps(request: ServerTransactionalRequest): Observable<Response<TransactionalStepResponse>> =
        executeCall(service.getSteps(request))

    fun validateStep(request: ValidateTransactionRequest): Observable<Response<ValidationStepResponse>> =
        executeCall(service.validateStep(request))

    fun requestQuickReminderInfo(request: QuickReminderRequest): Observable<Response<QuickReminderResponse>> =
        executeCall(service.getQuickReminderInfo(request))

    fun requestLocationMapInfo(request: LocationMapRequest): Observable<Response<LocationMapResponse>> =
        executeCall(service.getLocationMapInfo(request))

    fun clearTransaction(request: ClearTransactionRequest): Observable<Response<Void>> =
        executeCall(service.clearTransaction(request))
}
