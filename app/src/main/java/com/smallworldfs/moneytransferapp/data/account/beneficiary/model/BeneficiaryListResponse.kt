package com.smallworldfs.moneytransferapp.data.account.beneficiary.model

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Beneficiary
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class BeneficiaryListResponse(
    var msg: String = STRING_EMPTY,
    var beneficiaries: List<Beneficiary> = listOf()
) {
    fun getBeneficiariesSortedById(): List<Beneficiary> {
        return beneficiaries.sortedBy { it.id }
    }
}
