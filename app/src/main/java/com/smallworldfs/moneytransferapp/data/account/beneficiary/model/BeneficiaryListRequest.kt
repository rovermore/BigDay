package com.smallworldfs.moneytransferapp.data.account.beneficiary.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class BeneficiaryListRequest(
    val filter: String = STRING_EMPTY,
    val offset: String = STRING_EMPTY,
    val limit: String = STRING_EMPTY,
    val userId: String = STRING_EMPTY,
    val userToken: String = STRING_EMPTY,
)
