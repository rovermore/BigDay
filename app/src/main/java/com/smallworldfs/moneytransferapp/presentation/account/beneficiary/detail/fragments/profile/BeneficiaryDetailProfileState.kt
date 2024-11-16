package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.fragments.profile

import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel
import java.io.Serializable

data class BeneficiaryDetailProfileState(
    var beneficiary: BeneficiaryUIModel = BeneficiaryUIModel()
) : Serializable
