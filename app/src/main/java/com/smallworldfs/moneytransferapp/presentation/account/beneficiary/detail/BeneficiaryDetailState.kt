package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail

import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel
import java.io.Serializable

data class BeneficiaryDetailState(
    val beneficiary: BeneficiaryUIModel = BeneficiaryUIModel()
) : Serializable
