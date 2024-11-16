package com.smallworldfs.moneytransferapp.data.account.beneficiary.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class BeneficiaryDeleteRequest(
    val beneficiaryId: String = STRING_EMPTY,
    val userId: String = STRING_EMPTY,
    val userToken: String = STRING_EMPTY,
)
