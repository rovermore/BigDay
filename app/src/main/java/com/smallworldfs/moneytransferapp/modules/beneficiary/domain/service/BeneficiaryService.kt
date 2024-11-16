package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.service

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.BeneficiariesServer
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.BeneficiaryRequest
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.CreateBeneficiaryRequest
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.EditBeneficiaryFormRequest
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.NewBeneficiaryFormRequest
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.UpdateBeneficiaryRequest
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.structure.EditBeneficiaryFormResponse
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.structure.NewBeneficiaryFormResponse
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.validation.BeneficiaryValidationResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.QueryMap
import rx.Observable

interface BeneficiaryService {

    @GET(EndPoint.BENEFICIARIES)
    fun getBeneficiaries(@QueryMap request: BeneficiaryRequest?): Observable<Response<BeneficiariesServer>?>?

    @GET(EndPoint.NEW_BENEFICIARY_GET_FORM)
    fun getBeneficiaryForm(
        @QueryMap request: NewBeneficiaryFormRequest?
    ): Observable<Response<NewBeneficiaryFormResponse>?>?

    @POST(EndPoint.CREATE_NEW_BENEFICIARY)
    fun createNewBeneficiary(
        @QueryMap request: CreateBeneficiaryRequest?
    ): Observable<Response<BeneficiaryValidationResponse>?>?

    @GET(EndPoint.EDIT_BENEFICIARY_GET_FORM)
    fun getEditBeneficiaryForm(
        @QueryMap request: EditBeneficiaryFormRequest?
    ): Observable<Response<EditBeneficiaryFormResponse>?>?

    @PUT(EndPoint.UPDATE_BENEFICIARY)
    fun updateBeneficiary(
        @QueryMap request: UpdateBeneficiaryRequest?
    ): Observable<Response<BeneficiaryValidationResponse>?>?
}
