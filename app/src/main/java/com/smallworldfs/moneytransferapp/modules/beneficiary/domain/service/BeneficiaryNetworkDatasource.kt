package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.service

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.BeneficiariesServer
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.BeneficiaryRequest
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.CreateBeneficiaryRequest
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.EditBeneficiaryFormRequest
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.NewBeneficiaryFormRequest
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.UpdateBeneficiaryRequest
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.structure.EditBeneficiaryFormResponse
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.structure.NewBeneficiaryFormResponse
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.validation.BeneficiaryValidationResponse
import retrofit2.Response
import rx.Observable

class BeneficiaryNetworkDatasource(
    private val service: BeneficiaryService
) : NetworkDatasource() {

    fun requestBeneficiaries(request: BeneficiaryRequest): Observable<Response<BeneficiariesServer>> =
        executeCall(service.getBeneficiaries(request))

    fun requestNewBeneficiaryForm(request: NewBeneficiaryFormRequest): Observable<Response<NewBeneficiaryFormResponse>> =
        executeCall(service.getBeneficiaryForm(request))

    fun requestCreateNewBeneficiary(request: CreateBeneficiaryRequest): Observable<Response<BeneficiaryValidationResponse>> =
        executeCall(service.createNewBeneficiary(request))

    fun requestUpdateBeneficiary(request: UpdateBeneficiaryRequest): Observable<Response<BeneficiaryValidationResponse>> =
        executeCall(service.updateBeneficiary(request))

    fun requestEditBeneficiaryForm(request: EditBeneficiaryFormRequest): Observable<Response<EditBeneficiaryFormResponse>> =
        executeCall(service.getEditBeneficiaryForm(request))
}
