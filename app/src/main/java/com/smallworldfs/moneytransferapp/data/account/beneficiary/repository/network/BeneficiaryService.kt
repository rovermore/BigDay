package com.smallworldfs.moneytransferapp.data.account.beneficiary.repository.network

import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.BeneficiaryActivityListResponse
import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.BeneficiaryDeleteResponse
import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.BeneficiaryListResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface BeneficiaryService {

    @GET(EndPoint.BENEFICIARIES)
    fun requestBeneficiaryList(
        @Query("userToken") userToken: String,
        @Query("userId") userId: String,
        @Query("filter") filter: String,
        @Query("offset") offset: String,
        @Query("limit") limit: String
    ): Call<BeneficiaryListResponse>

    @DELETE(EndPoint.DELETE_BENEFICIARY)
    fun deleteBeneficiary(
        @Query("userToken") userToken: String,
        @Query("userId") userId: String,
        @Query("beneficiaryId") beneficiaryId: String
    ): Call<BeneficiaryDeleteResponse>

    @GET(EndPoint.USER_ACTIVITY)
    fun requestBeneficiaryActivityList(
        @Query("userToken") userToken: String,
        @Query("userId") userId: String,
        @Query("beneficiaryId") beneficiaryId: String
    ): Call<BeneficiaryActivityListResponse>
}
