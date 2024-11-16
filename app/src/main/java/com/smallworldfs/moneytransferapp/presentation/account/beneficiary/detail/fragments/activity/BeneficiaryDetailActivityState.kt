package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.fragments.activity

import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.TransactionsInfo
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.io.Serializable

data class BeneficiaryDetailActivityState(
    var beneficiary: BeneficiaryUIModel = BeneficiaryUIModel(),
    var transactionsInfo: TransactionsInfo = TransactionsInfo(),
    var transactions: ArrayList<Transaction> = arrayListOf(),
    var cancellationMessage: String = STRING_EMPTY
) : Serializable
