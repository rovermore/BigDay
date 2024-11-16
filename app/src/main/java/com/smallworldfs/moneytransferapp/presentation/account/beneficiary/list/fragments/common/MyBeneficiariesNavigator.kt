package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.common

import android.app.Activity
import android.content.Intent
import androidx.core.util.Pair
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryActivity
import com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.BeneficiaryDetailActivity
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.BeneficiaryDetailState
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.INPUT_STATE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyBeneficiariesNavigator @Inject constructor(
    private val activity: Activity
) {
    fun navigateToBeneficiaryDetail(beneficiary: BeneficiaryUIModel) {
        val intent = Intent(activity, BeneficiaryDetailActivity::class.java)
        intent.putExtra(INPUT_STATE, BeneficiaryDetailState(beneficiary))
        activity.startActivityForResult(intent, Constants.REQUEST_CODES.BENEFICIARY_DETAIL)
    }

    fun navigateToC2BActivity(
        fromTransactional: Boolean,
        country: Pair<String?, String?>
    ) {
        val intent = Intent(activity, C2BActivity::class.java)
        intent.putExtra(NewBeneficiaryActivity.EXTRA_KEY, country.first)
        intent.putExtra(NewBeneficiaryActivity.EXTRA_VALUE, country.second)
        intent.putExtra(C2BActivity.FROM_TRANSACTIONAL, fromTransactional)
        activity.startActivityForResult(intent, Constants.REQUEST_CODES.NEW_BENEFICIARY)
    }
}
