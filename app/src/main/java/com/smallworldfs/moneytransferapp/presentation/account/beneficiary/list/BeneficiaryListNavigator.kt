package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryStepCountryActivity
import com.smallworldfs.moneytransferapp.utils.Constants.REQUEST_CODES
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeneficiaryListNavigator @Inject constructor(
    private val activity: Activity
) {

    fun navigateToNewBeneficiaryStepCountry() {
        val intent = Intent(activity, NewBeneficiaryStepCountryActivity::class.java)
        activity.startActivityForResult(intent, REQUEST_CODES.NEW_BENEFICIARY)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }
}
