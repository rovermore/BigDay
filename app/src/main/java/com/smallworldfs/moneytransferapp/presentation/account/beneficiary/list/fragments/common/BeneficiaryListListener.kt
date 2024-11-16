package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.common

import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel

interface BeneficiaryListListener {
    fun onItemClick(item: BeneficiaryUIModel, position: Int)
}
